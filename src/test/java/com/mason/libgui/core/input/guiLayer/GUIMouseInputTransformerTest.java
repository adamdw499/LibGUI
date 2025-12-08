package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.core.input.mouse.MouseInputType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Function;

class GUIMouseInputTransformerTest{

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

    private GUIMouseInputTransformer transformerWith(
            TestGUIInputSocket delegate,
            Function<MouseInputEvent, MouseInputEvent> transform
    ) {
        GUIMouseInputTransformer t = new GUIMouseInputTransformer();
        t.setDelegateSocket(delegate);
        t.setTransform(transform);
        return t;
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

    // --- Key events: should be passed through SimpleGUIInputGate unchanged ---

    @Test
    void keyEvents_areDelegatedWithoutUsingTransform() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        // Transform that would explode if called on key events (sanity check)
        Function<MouseInputEvent, MouseInputEvent> transform = e -> {
            fail("Transform should not be called for key events");
            return e;
        };

        GUIMouseInputTransformer transformer = transformerWith(delegate, transform);

        KeyEvent typed = new KeyEvent(
                source(),
                KeyEvent.KEY_TYPED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_UNDEFINED,
                'a'
        );
        KeyEvent pressed = new KeyEvent(
                source(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_A,
                'a'
        );
        KeyEvent released = new KeyEvent(
                source(),
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_A,
                'a'
        );

        transformer.keyTyped(typed);
        transformer.keyPressed(pressed);
        transformer.keyReleased(released);

        assertSame(typed, delegate.lastKeyTyped);
        assertSame(pressed, delegate.lastKeyPressed);
        assertSame(released, delegate.lastKeyReleased);
    }

    // --- Mouse events: transform should be applied before delegating ---

    @Test
    void onMouseMoved_appliesTransformAndDelegatesTransformedEvent() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();

        Function<MouseInputEvent, MouseInputEvent> transform = e -> {
            // Translate coord by (+5, +7) using setCoordRelativeTo
            Coord c = e.getCoord();
            Coord offset = new Coord(c.x() - 5, c.y() - 7);
            e.setCoordRelativeTo(offset); // new = old - offset = (5,7) more
            return e;
        };

        GUIMouseInputTransformer transformer = transformerWith(delegate, transform);

        MouseInputEvent input = mouseInputEvent(MouseEvent.MOUSE_MOVED, 10, 20);
        transformer.onMouseMoved(input);

        assertEquals("moved", delegate.lastMouseMethod);
        assertSame(input, delegate.lastMouseEvent); // same instance
        assertEquals(new Coord(5, 7), delegate.lastMouseEvent.getCoord());
    }

    @Test
    void onMousePressed_usesInstanceReturnedFromTransformFunction() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();

        Function<MouseInputEvent, MouseInputEvent> transform = original -> {
            // Replace with a completely new MouseInputEvent
            MouseEvent raw = new MouseEvent(
                    source(),
                    MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(),
                    0,
                    100, 200,
                    1,
                    false
            );
            return new MouseInputEvent(raw);
        };

        GUIMouseInputTransformer transformer = transformerWith(delegate, transform);

        MouseInputEvent original = mouseInputEvent(MouseEvent.MOUSE_PRESSED, 10, 20);
        transformer.onMousePressed(original);

        assertEquals("pressed", delegate.lastMouseMethod);
        assertNotSame(original, delegate.lastMouseEvent);
        assertEquals(new Coord(100, 200), delegate.lastMouseEvent.getCoord());
        assertEquals(MouseInputType.PRESS, delegate.lastMouseEvent.getType());
    }

    @Test
    void onMouseWheel_appliesTransformAndDelegates() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();

        Function<MouseInputEvent, MouseInputEvent> transform = e -> {
            // Just to prove transform is called; adjust coord
            e.setCoordRelativeTo(new Coord(-10, -20)); // add (10,20)
            return e;
        };

        GUIMouseInputTransformer transformer = transformerWith(delegate, transform);

        MouseWheelEvent rawWheel = new MouseWheelEvent(
                source(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                1, 2,
                0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                -1
        );
        MouseInputEvent input = new MouseInputEvent(rawWheel);

        transformer.onMouseWheel(input);

        assertEquals("wheel", delegate.lastMouseMethod);
        assertEquals(new Coord(11, 22), delegate.lastMouseEvent.getCoord());
        assertEquals(MouseInputType.WHEEL, delegate.lastMouseEvent.getType());
    }

}