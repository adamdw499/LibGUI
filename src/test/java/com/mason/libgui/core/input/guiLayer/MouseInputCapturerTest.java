package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

class MouseInputCapturerTest{

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

    private MouseInputCapturer capturerWith(TestGUIInputSocket delegate) {
        MouseInputCapturer capturer = new MouseInputCapturer();
        capturer.setDelegateSocket(delegate);
        return capturer;
    }

    private MouseInputEvent mouseEvent(int id, int x, int y) {
        MouseEvent raw = new MouseEvent(
                new JButton(),
                id,
                System.currentTimeMillis(),
                0,
                x, y,
                1,
                false
        );
        return new MouseInputEvent(raw);
    }

    @Test
    void withinBounds_alwaysReturnsTrueForAnyCoord() {
        MouseInputCapturer capturer = new MouseInputCapturer();

        assertTrue(capturer.withinBounds(new Coord(0, 0)));
        assertTrue(capturer.withinBounds(new Coord(10, 20)));
        assertTrue(capturer.withinBounds(new Coord(-5, -7)));
        assertTrue(capturer.withinBounds(new Coord(Integer.MAX_VALUE, Integer.MIN_VALUE)));
    }

    @Test
    void keyEvents_areStillDelegatedViaSimpleGUIInputGateBehaviour() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        MouseInputCapturer capturer = capturerWith(delegate);

        KeyEvent typed = new KeyEvent(
                new JButton(),
                KeyEvent.KEY_TYPED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_UNDEFINED,
                'a'
        );
        KeyEvent pressed = new KeyEvent(
                new JButton(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_A,
                'a'
        );
        KeyEvent released = new KeyEvent(
                new JButton(),
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_A,
                'a'
        );

        capturer.keyTyped(typed);
        capturer.keyPressed(pressed);
        capturer.keyReleased(released);

        assertSame(typed, delegate.lastKeyTyped);
        assertSame(pressed, delegate.lastKeyPressed);
        assertSame(released, delegate.lastKeyReleased);
    }

    @Test
    void mouseEvents_areDelegatedToDelegateSocket() {
        TestGUIInputSocket delegate = new TestGUIInputSocket();
        MouseInputCapturer capturer = capturerWith(delegate);

        MouseInputEvent moved = mouseEvent(MouseEvent.MOUSE_MOVED, 10, 20);
        MouseInputEvent pressed = mouseEvent(MouseEvent.MOUSE_PRESSED, 15, 25);
        MouseInputEvent clicked = mouseEvent(MouseEvent.MOUSE_CLICKED, 30, 40);

        capturer.onMouseMoved(moved);
        assertEquals("moved", delegate.lastMouseMethod);
        assertSame(moved, delegate.lastMouseEvent);

        capturer.onMousePressed(pressed);
        assertEquals("pressed", delegate.lastMouseMethod);
        assertSame(pressed, delegate.lastMouseEvent);

        capturer.onMouseClicked(clicked);
        assertEquals("clicked", delegate.lastMouseMethod);
        assertSame(clicked, delegate.lastMouseEvent);
    }

}