package com.mason.libgui.core.input.componentLayer;

import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.core.input.mouse.MouseInputType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class UIComponentManagerInputDistributorTest {


    private static class TestKeyListener implements KeyListener{
        final String name;
        final List<String> callOrder;

        KeyEvent lastTyped;
        KeyEvent lastPressed;
        KeyEvent lastReleased;

        TestKeyListener(String name, List<String> callOrder) {
            this.name = name;
            this.callOrder = callOrder;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            callOrder.add(name + ":typed");
            lastTyped = e;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            callOrder.add(name + ":pressed");
            lastPressed = e;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            callOrder.add(name + ":released");
            lastReleased = e;
        }
    }

    private static class TestBoundedMouseListener implements BoundedMouseInputListener{

        boolean withinBoundsReturn = true;

        String lastMethod;
        MouseInputEvent lastEvent;

        @Override
        public boolean withinBounds(Coord c) {
            return withinBoundsReturn;
        }

        @Override
        public void onMouseMoved(MouseInputEvent e) {
            lastMethod = "moved";
            lastEvent = e;
        }

        @Override
        public void onMouseDragged(MouseInputEvent e) {
            lastMethod = "dragged";
            lastEvent = e;
        }

        @Override
        public void onMousePressed(MouseInputEvent e) {
            lastMethod = "pressed";
            lastEvent = e;
        }

        @Override
        public void onMouseReleased(MouseInputEvent e) {
            lastMethod = "released";
            lastEvent = e;
        }

        @Override
        public void onMouseClicked(MouseInputEvent e) {
            lastMethod = "clicked";
            lastEvent = e;
        }

        @Override
        public void onMouseWheel(MouseInputEvent e) {
            lastMethod = "wheel";
            lastEvent = e;
        }
    }

    private Component source() {
        return new JButton();
    }

    private MouseInputEvent mouseInputEvent(int id, int x, int y) {
        MouseEvent raw = new MouseEvent(
                source(),
                id,
                System.currentTimeMillis(),
                0,
                x, y,
                1,
                false
        );
        return new MouseInputEvent(raw);
    }

    private MouseInputEvent wheelInputEvent(int x, int y, int rotation) {
        MouseWheelEvent raw = new MouseWheelEvent(
                source(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                rotation
        );
        return new MouseInputEvent(raw);
    }

    // --- Key listener behaviour ---

    @Test
    void keyListeners_receiveEventsInReverseOrderOfAddition() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();
        List<String> calls = new ArrayList<>();

        TestKeyListener l1 = new TestKeyListener("L1", calls);
        TestKeyListener l2 = new TestKeyListener("L2", calls);

        distributor.addKeyListener(l1);
        distributor.addKeyListener(l2);

        KeyEvent e = new KeyEvent(
                source(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_A,
                'a'
        );

        distributor.keyPressed(e);

        assertEquals(List.of("L2:pressed", "L1:pressed"), calls);
        assertSame(e, l1.lastPressed);
        assertSame(e, l2.lastPressed);
    }

    @Test
    void removedKeyListener_noLongerReceivesEvents() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();
        List<String> calls = new ArrayList<>();

        TestKeyListener l1 = new TestKeyListener("L1", calls);
        TestKeyListener l2 = new TestKeyListener("L2", calls);

        distributor.addKeyListener(l1);
        distributor.addKeyListener(l2);
        distributor.removeKeyListener(l2);

        KeyEvent e = new KeyEvent(
                source(),
                KeyEvent.KEY_TYPED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_UNDEFINED,
                'x'
        );

        distributor.keyTyped(e);

        assertEquals(List.of("L1:typed"), calls);
        assertSame(e, l1.lastTyped);
        assertNull(l2.lastTyped);
    }

    // --- Mouse listener dispatch / bounds / ordering ---

    @Test
    void mouseEvent_deliveredToLastAddedListenerWhoseBoundsContainCoord() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();

        TestBoundedMouseListener l1 = new TestBoundedMouseListener();
        TestBoundedMouseListener l2 = new TestBoundedMouseListener();

        l1.withinBoundsReturn = true;
        l2.withinBoundsReturn = true;

        distributor.addMouseInputListener(l1);
        distributor.addMouseInputListener(l2); // last added, should win

        MouseInputEvent event = mouseInputEvent(MouseEvent.MOUSE_MOVED, 10, 20);
        distributor.onMouseInput(event);

        // because of OrderedListenerRegister's descendingIterator:
        // iteration order is [l2, l1]; l2 matches first and break occurs
        assertEquals("moved", l2.lastMethod);
        assertSame(event, l2.lastEvent);

        assertNull(l1.lastMethod);
        assertNull(l1.lastEvent);
    }

    @Test
    void mouseEvent_skipsListenersOutOfBounds_usesFirstInBoundsInIterationOrder() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();

        TestBoundedMouseListener l1 = new TestBoundedMouseListener();
        TestBoundedMouseListener l2 = new TestBoundedMouseListener();

        // l1 out of bounds; l2 in bounds
        l1.withinBoundsReturn = false;
        l2.withinBoundsReturn = true;

        distributor.addMouseInputListener(l1);
        distributor.addMouseInputListener(l2);

        MouseInputEvent event = mouseInputEvent(MouseEvent.MOUSE_PRESSED, 5, 5);
        distributor.onMouseInput(event);

        // iteration order: l2, then l1
        // l2 is in bounds so gets event; l1 is never touched
        assertEquals("pressed", l2.lastMethod);
        assertSame(event, l2.lastEvent);

        assertNull(l1.lastMethod);
        assertNull(l1.lastEvent);
    }

    @Test
    void mouseEvent_notDeliveredWhenNoListenerInBounds() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();

        TestBoundedMouseListener l1 = new TestBoundedMouseListener();
        TestBoundedMouseListener l2 = new TestBoundedMouseListener();
        l1.withinBoundsReturn = false;
        l2.withinBoundsReturn = false;

        distributor.addMouseInputListener(l1);
        distributor.addMouseInputListener(l2);

        MouseInputEvent event = mouseInputEvent(MouseEvent.MOUSE_RELEASED, 0, 0);
        distributor.onMouseInput(event);

        assertNull(l1.lastMethod);
        assertNull(l2.lastMethod);
    }

    @Test
    void removeMouseInputListener_stopsReceivingEvents() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();

        TestBoundedMouseListener l1 = new TestBoundedMouseListener();
        l1.withinBoundsReturn = true;

        distributor.addMouseInputListener(l1);
        distributor.removeMouseInputListener(l1);

        MouseInputEvent event = mouseInputEvent(MouseEvent.MOUSE_CLICKED, 1, 1);
        distributor.onMouseInput(event);

        assertNull(l1.lastMethod);
        assertNull(l1.lastEvent);
    }

    // --- Dispatch mapping: MouseInputType -> correct listener method ---

    @Test
    void dispatch_drag_callsOnMouseDragged() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();
        TestBoundedMouseListener listener = new TestBoundedMouseListener();
        distributor.addMouseInputListener(listener);

        MouseInputEvent event = mouseInputEvent(MouseEvent.MOUSE_DRAGGED, 1, 2);
        distributor.onMouseInput(event);

        assertEquals("dragged", listener.lastMethod);
    }

    @Test
    void dispatch_press_release_click_wheel_callCorrectMethods() {
        UIComponentManagerInputDistributor distributor = new UIComponentManagerInputDistributor();
        TestBoundedMouseListener listener = new TestBoundedMouseListener();
        distributor.addMouseInputListener(listener);

        MouseInputEvent press = mouseInputEvent(MouseEvent.MOUSE_PRESSED, 1, 1);
        distributor.onMouseInput(press);
        assertEquals("pressed", listener.lastMethod);

        MouseInputEvent release = mouseInputEvent(MouseEvent.MOUSE_RELEASED, 2, 2);
        distributor.onMouseInput(release);
        assertEquals("released", listener.lastMethod);

        MouseInputEvent click = mouseInputEvent(MouseEvent.MOUSE_CLICKED, 3, 3);
        distributor.onMouseInput(click);
        assertEquals("clicked", listener.lastMethod);

        MouseInputEvent wheel = wheelInputEvent(4, 4, -1);
        distributor.onMouseInput(wheel);
        assertEquals("wheel", listener.lastMethod);
        assertEquals(MouseInputType.WHEEL, listener.lastEvent.getType());
    }
}