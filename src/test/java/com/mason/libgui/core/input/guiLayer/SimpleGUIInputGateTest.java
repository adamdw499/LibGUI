package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class SimpleGUIInputGateTest{

    private static class TestGUIInputSocket implements GUIInputSocket {

        KeyEvent lastKeyTyped;
        KeyEvent lastKeyPressed;
        KeyEvent lastKeyReleased;

        String lastMouseMethod;
        MouseInputEvent lastMouseEvent;

        @Override
        public void keyTyped(KeyEvent e) {
            lastKeyTyped = e;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            lastKeyPressed = e;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            lastKeyReleased = e;
        }

        @Override
        public void onMouseMoved(MouseInputEvent e) {
            lastMouseMethod = "moved";
            lastMouseEvent = e;
        }

        @Override
        public void onMouseDragged(MouseInputEvent e) {
            lastMouseMethod = "dragged";
            lastMouseEvent = e;
        }

        @Override
        public void onMousePressed(MouseInputEvent e) {
            lastMouseMethod = "pressed";
            lastMouseEvent = e;
        }

        @Override
        public void onMouseReleased(MouseInputEvent e) {
            lastMouseMethod = "released";
            lastMouseEvent = e;
        }

        @Override
        public void onMouseClicked(MouseInputEvent e) {
            lastMouseMethod = "clicked";
            lastMouseEvent = e;
        }

        @Override
        public void onMouseWheel(MouseInputEvent e) {
            lastMouseMethod = "wheel";
            lastMouseEvent = e;
        }
    }

    private SimpleGUIInputGate gateWith(TestGUIInputSocket delegate) {
        SimpleGUIInputGate gate = new SimpleGUIInputGate();
        gate.setDelegateSocket(delegate);
        return gate;
    }

    private MouseInputEvent mouseEvent(int id) {
        MouseEvent raw = new MouseEvent(
                new JButton(), id, System.currentTimeMillis(),
                0, 10, 20, 1, false
        );
        return new MouseInputEvent(raw);
    }

    // --- Key events ---

    @Test
    void keyTyped_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_TYPED, System.currentTimeMillis(),
                0, KeyEvent.VK_UNDEFINED, 'a'
        );

        gate.keyTyped(event);

        assertSame(event, delegate.lastKeyTyped);
        assertNull(delegate.lastKeyPressed);
        assertNull(delegate.lastKeyReleased);
    }

    @Test
    void keyPressed_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                0, KeyEvent.VK_A, 'a'
        );

        gate.keyPressed(event);

        assertSame(event, delegate.lastKeyPressed);
        assertNull(delegate.lastKeyTyped);
        assertNull(delegate.lastKeyReleased);
    }

    @Test
    void keyReleased_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
                0, KeyEvent.VK_A, 'a'
        );

        gate.keyReleased(event);

        assertSame(event, delegate.lastKeyReleased);
        assertNull(delegate.lastKeyTyped);
        assertNull(delegate.lastKeyPressed);
    }


    @Test
    void onMouseMoved_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseInputEvent e = mouseEvent(MouseEvent.MOUSE_MOVED);
        gate.onMouseMoved(e);

        assertEquals("moved", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

    @Test
    void onMouseDragged_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseInputEvent e = mouseEvent(MouseEvent.MOUSE_DRAGGED);
        gate.onMouseDragged(e);

        assertEquals("dragged", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

    // --- Mouse press/release/click ---

    @Test
    void onMousePressed_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseInputEvent e = mouseEvent(MouseEvent.MOUSE_PRESSED);
        gate.onMousePressed(e);

        assertEquals("pressed", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

    @Test
    void onMouseReleased_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseInputEvent e = mouseEvent(MouseEvent.MOUSE_RELEASED);
        gate.onMouseReleased(e);

        assertEquals("released", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

    @Test
    void onMouseClicked_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseInputEvent e = mouseEvent(MouseEvent.MOUSE_CLICKED);
        gate.onMouseClicked(e);

        assertEquals("clicked", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

    // --- Mouse wheel ---

    @Test
    void onMouseWheel_isDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        SimpleGUIInputGate gate = gateWith(delegate);

        MouseWheelEvent rawWheel = new MouseWheelEvent(
                new JButton(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                10, 20,
                0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                -1
        );
        MouseInputEvent e = new MouseInputEvent(rawWheel);

        gate.onMouseWheel(e);

        assertEquals("wheel", delegate.lastMouseMethod);
        assertSame(e, delegate.lastMouseEvent);
    }

}