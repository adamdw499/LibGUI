package com.mason.libgui.components.panes;

import com.mason.libgui.core.input.guiLayer.GUIInputSocket;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class PaneGUIInputTranslatorTest{

    private static class TestGUIInputSocket implements GUIInputSocket{

        String lastMouseMethod;
        MouseInputEvent lastMouseEvent;

        KeyEvent lastKeyTyped;
        KeyEvent lastKeyPressed;
        KeyEvent lastKeyReleased;

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

    /** RectQuery used as the boundary; withinBounds is implemented via a Rect. */
    private static class TestBoundaryRect implements RectQuery {

        private final Coord coord;
        private final Size size;

        TestBoundaryRect(Coord coord, Size size) {
            this.coord = coord;
            this.size = size;
        }

        @Override
        public Coord getCoord() {
            return coord;
        }

        @Override
        public Size getSize() {
            return size;
        }

        @Override
        public boolean withinBounds(Coord c) {
            Rect r = new Rect(coord.x(), coord.y(), size.width(), size.height());
            return r.withinBounds(c);
        }
    }

    private Component source() {
        return new JButton();
    }

    private MouseInputEvent mouseEvent(int id, int x, int y) {
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

    private MouseInputEvent wheelEvent(int x, int y, int rotation) {
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

    // ---------- Tests ----------

    @Test
    void withinBounds_delegatesToBoundaryRect() {
        TestBoundaryRect boundary = new TestBoundaryRect(new Coord(10, 20), new Size(30, 40));
        PaneGUIInputTranslator translator = new PaneGUIInputTranslator(boundary);

        // Inside the boundary: x ∈ [10,40), y ∈ [20,60)
        assertTrue(translator.withinBounds(new Coord(10, 20)));
        assertTrue(translator.withinBounds(new Coord(39, 59)));

        // Outside
        assertFalse(translator.withinBounds(new Coord(9, 20)));
        assertFalse(translator.withinBounds(new Coord(10, 19)));
        assertFalse(translator.withinBounds(new Coord(40, 30)));
        assertFalse(translator.withinBounds(new Coord(15, 60)));
    }

    @Test
    void mouseMoved_eventIsRelativizedToBoundaryBeforeDelegation() {
        TestBoundaryRect boundary = new TestBoundaryRect(new Coord(10, 20), new Size(100, 100));
        PaneGUIInputTranslator translator = new PaneGUIInputTranslator(boundary);

        TestGUIInputSocket delegate = new TestGUIInputSocket();
        translator.setDelegateSocket(delegate);

        // Raw mouse at (25,29) -> relative to (10,20) should be (15,9)
        MouseInputEvent input = mouseEvent(MouseEvent.MOUSE_MOVED, 25, 29);

        translator.onMouseMoved(input);

        assertEquals("moved", delegate.lastMouseMethod);
        assertSame(input, delegate.lastMouseEvent, "Transformer should reuse the same MouseInputEvent instance");
        assertEquals(new Coord(15, 9), delegate.lastMouseEvent.getCoord());
    }

    @Test
    void mousePressed_eventIsRelativizedToBoundary() {
        TestBoundaryRect boundary = new TestBoundaryRect(new Coord(5, 5), new Size(50, 50));
        PaneGUIInputTranslator translator = new PaneGUIInputTranslator(boundary);

        TestGUIInputSocket delegate = new TestGUIInputSocket();
        translator.setDelegateSocket(delegate);

        MouseInputEvent input = mouseEvent(MouseEvent.MOUSE_PRESSED, 8, 10);
        translator.onMousePressed(input);

        assertEquals("pressed", delegate.lastMouseMethod);
        assertEquals(new Coord(3, 5), delegate.lastMouseEvent.getCoord());
    }

    @Test
    void mouseDragged_eventIsRelativizedToBoundary() {
        TestBoundaryRect boundary = new TestBoundaryRect(new Coord(0, 10), new Size(50, 50));
        PaneGUIInputTranslator translator = new PaneGUIInputTranslator(boundary);

        TestGUIInputSocket delegate = new TestGUIInputSocket();
        translator.setDelegateSocket(delegate);

        MouseInputEvent input = mouseEvent(MouseEvent.MOUSE_DRAGGED, 7, 15);
        translator.onMouseDragged(input);

        // Relative to (0,10) -> (7,5)
        assertEquals("dragged", delegate.lastMouseMethod);
        assertEquals(new Coord(7, 5), delegate.lastMouseEvent.getCoord());
    }

    @Test
    void mouseWheel_eventIsRelativizedToBoundaryAndWheelMotionPreserved() {
        TestBoundaryRect boundary = new TestBoundaryRect(new Coord(10, 0), new Size(100, 100));
        PaneGUIInputTranslator translator = new PaneGUIInputTranslator(boundary);

        TestGUIInputSocket delegate = new TestGUIInputSocket();
        translator.setDelegateSocket(delegate);

        MouseInputEvent input = wheelEvent(15, 5, -3);
        translator.onMouseWheel(input);

        assertEquals("wheel", delegate.lastMouseMethod);
        assertEquals(new Coord(5, 5), delegate.lastMouseEvent.getCoord()); // (15-10,5-0)
        assertEquals(-3, delegate.lastMouseEvent.getWheelMotion());
    }

}