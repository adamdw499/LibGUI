package com.mason.libgui.components.behaviour.drag;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class AbstractDragBehaviourTest{

    private static class TestInputSource implements GUIInputRegister<BoundedMouseInputListener>{

        final List<BoundedMouseInputListener> addedMouse = new ArrayList<>();
        final List<BoundedMouseInputListener> removedMouse = new ArrayList<>();

        final List<KeyListener> addedKeys = new ArrayList<>();
        final List<KeyListener> removedKeys = new ArrayList<>();

        @Override
        public void addMouseInputListener(BoundedMouseInputListener listener) {
            addedMouse.add(listener);
        }

        @Override
        public void removeMouseInputListener(BoundedMouseInputListener listener) {
            removedMouse.add(listener);
        }

        @Override
        public void addKeyListener(KeyListener listener) {
            addedKeys.add(listener);
        }

        @Override
        public void removeKeyListener(KeyListener listener) {
            removedKeys.add(listener);
        }
    }

    private static class TestDragBehaviour extends AbstractDragBehaviour {

        boolean startCalled = false;
        boolean incrementCalled = false;
        boolean releaseCalled = false;

        MouseInputEvent startEvent;
        MouseInputEvent incrementEvent;
        MouseInputEvent releaseEvent;

        @Override
        public boolean withinBounds(Coord c) {
            return true;
        }

        @Override
        protected void onDragStart(MouseInputEvent event) {
            startCalled = true;
            startEvent = event;
        }

        @Override
        protected void onDragIncrement(MouseInputEvent event) {
            incrementCalled = true;
            incrementEvent = event;
        }

        @Override
        protected void onDragRelease(MouseInputEvent event) {
            releaseCalled = true;
            releaseEvent = event;
        }

        // GUIInputSocket methods we don't care about for these tests:
        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}

        @Override public void onMouseMoved(MouseInputEvent e) {}
        @Override public void onMouseClicked(MouseInputEvent e) {}
        @Override public void onMouseWheel(MouseInputEvent e) {}
    }

    private MouseInputEvent mouseEvent(int id, int x, int y) {
        MouseEvent raw = new MouseEvent(
                new JButton(),
                id,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false
        );
        return new MouseInputEvent(raw);
    }


    @Test
    void setInputSource_registersBehaviourItselfAsMouseListener() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();

        behaviour.setInputSource(source);

        assertEquals(1, source.addedMouse.size());
        assertSame(behaviour, source.addedMouse.get(0));
    }

    @Test
    void unsetInputSource_removesBehaviourItselfAsMouseListener() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();

        behaviour.setInputSource(source);
        behaviour.unsetInputSource(source);

        assertEquals(1, source.removedMouse.size());
        assertSame(behaviour, source.removedMouse.get(0));
    }

    @Test
    void onMousePressed_whenNotDragging_startsDrag_andCapturesMouse() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();
        behaviour.setInputSource(source);

        MouseInputEvent event = mouseEvent(MouseEvent.MOUSE_PRESSED, 10, 20);

        // Before press
        assertFalse(behaviour.isCurrentlyDragging());
        assertEquals(1, source.addedMouse.size()); // only 'this' so far

        behaviour.onMousePressed(event);

        assertTrue(behaviour.isCurrentlyDragging());
        assertTrue(behaviour.startCalled);
        assertSame(event, behaviour.startEvent);

        // captureMouse() should add an extra listener (the internal MouseInputCapturer)
        assertEquals(2, source.addedMouse.size());
        BoundedMouseInputListener internalCapturer = source.addedMouse.get(1);
        assertNotSame(behaviour, internalCapturer);
    }

    @Test
    void onMousePressed_whenAlreadyDragging_doesNothing() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();
        behaviour.setInputSource(source);

        MouseInputEvent first = mouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0);
        MouseInputEvent second = mouseEvent(MouseEvent.MOUSE_PRESSED, 5, 5);

        behaviour.onMousePressed(first);

        assertTrue(behaviour.isCurrentlyDragging());
        assertTrue(behaviour.startCalled);
        assertSame(first, behaviour.startEvent);
        int addedCountAfterFirst = source.addedMouse.size();

        // Reset flag to see if it's called again
        behaviour.startCalled = false;

        behaviour.onMousePressed(second);

        assertTrue(behaviour.isCurrentlyDragging(), "Should still be dragging");
        assertFalse(behaviour.startCalled, "onDragStart should not be called again");
        assertEquals(addedCountAfterFirst, source.addedMouse.size(),
                "captureMouse should not be called again while dragging");
    }

    @Test
    void onMouseDragged_onlyCallsIncrementWhenDragging() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();
        behaviour.setInputSource(source);

        MouseInputEvent dragEvent = mouseEvent(MouseEvent.MOUSE_DRAGGED, 10, 20);

        // Not dragging yet
        behaviour.onMouseDragged(dragEvent);
        assertFalse(behaviour.incrementCalled);

        // Start drag
        behaviour.onMousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0));

        behaviour.onMouseDragged(dragEvent);
        assertTrue(behaviour.incrementCalled);
        assertSame(dragEvent, behaviour.incrementEvent);
    }

    @Test
    void onMouseReleased_whenDragging_stopsDrag_releasesMouse_andCallsReleaseHook() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();
        behaviour.setInputSource(source);

        // Start drag
        MouseInputEvent press = mouseEvent(MouseEvent.MOUSE_PRESSED, 1, 1);
        behaviour.onMousePressed(press);

        int addedCount = source.addedMouse.size();
        BoundedMouseInputListener internalCapturer = source.addedMouse.get(1);

        MouseInputEvent releaseEvent = mouseEvent(MouseEvent.MOUSE_RELEASED, 2, 2);
        behaviour.onMouseReleased(releaseEvent);

        assertFalse(behaviour.isCurrentlyDragging());
        assertTrue(behaviour.releaseCalled);
        assertSame(releaseEvent, behaviour.releaseEvent);

        // releaseMouse() should remove the internal capturer, not 'this'
        assertEquals(1, source.removedMouse.size());
        assertSame(internalCapturer, source.removedMouse.get(0));
        assertEquals(addedCount, source.addedMouse.size());
    }

    @Test
    void onMouseReleased_whenNotDragging_doesNothing() {
        TestInputSource source = new TestInputSource();
        TestDragBehaviour behaviour = new TestDragBehaviour();
        behaviour.setInputSource(source);

        MouseInputEvent releaseEvent = mouseEvent(MouseEvent.MOUSE_RELEASED, 10, 10);
        behaviour.onMouseReleased(releaseEvent);

        assertFalse(behaviour.isCurrentlyDragging());
        assertFalse(behaviour.releaseCalled);
        assertTrue(source.removedMouse.isEmpty(), "No listeners should be removed");
    }

}