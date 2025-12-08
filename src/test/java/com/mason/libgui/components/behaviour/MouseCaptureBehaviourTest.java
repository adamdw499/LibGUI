package com.mason.libgui.components.behaviour;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.guiLayer.GUIInputSocket;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

class MouseCaptureBehaviourTest{

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

    private static class TestInputSource implements GUIInputRegister<BoundedMouseInputListener>{

        BoundedMouseInputListener lastAddedMouseListener;
        BoundedMouseInputListener lastRemovedMouseListener;
        int addMouseCount = 0;
        int removeMouseCount = 0;

        @Override
        public void addMouseInputListener(BoundedMouseInputListener listener) {
            lastAddedMouseListener = listener;
            addMouseCount++;
        }

        @Override
        public void removeMouseInputListener(BoundedMouseInputListener listener) {
            lastRemovedMouseListener = listener;
            removeMouseCount++;
        }

        // Key listener methods are irrelevant here, so they can be no-ops
        @Override
        public void addKeyListener(KeyListener listener) { }

        @Override
        public void removeKeyListener(KeyListener listener) { }
    }

    private MouseInputEvent mouseMovedEvent(int x, int y) {
        MouseEvent raw = new MouseEvent(
                new JButton(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false
        );
        return new MouseInputEvent(raw);
    }


    @Test
    void captureMouse_addsInternalCapturerToInputSource_andReleaseRemovesSameInstance() {
        TestInputSource inputSource = new TestInputSource();
        MouseCaptureBehaviour behaviour = MouseCaptureBehaviour.buildWithoutDelegateSocket();
        behaviour.setInputRegister(inputSource);

        behaviour.captureMouse();

        assertNotNull(inputSource.lastAddedMouseListener, "captureMouse should register a listener");
        assertEquals(1, inputSource.addMouseCount);

        BoundedMouseInputListener added = inputSource.lastAddedMouseListener;

        behaviour.releaseMouse();

        assertEquals(1, inputSource.removeMouseCount);
        assertSame(added, inputSource.lastRemovedMouseListener,
                "releaseMouse should remove the same listener that was added");
    }

    @Test
    void buildWithDelegateSocket_setsDelegateOnInternalMouseCapturer() {
        TestGUIInputSocket guiSocket = new TestGUIInputSocket();
        MouseCaptureBehaviour behaviour = MouseCaptureBehaviour.buildWithDelegateSocket(guiSocket);

        TestInputSource inputSource = new TestInputSource();
        behaviour.setInputRegister(inputSource);

        // When we capture, the internal MouseInputCapturer is added as the listener
        behaviour.captureMouse();
        assertNotNull(inputSource.lastAddedMouseListener);
        BoundedMouseInputListener capturer = inputSource.lastAddedMouseListener;

        // Simulate a mouse move through the captured listener
        MouseInputEvent event = mouseMovedEvent(10, 20);
        capturer.onMouseMoved(event);

        // If the delegate socket was wired correctly, it should see the event
        assertEquals("moved", guiSocket.lastMouseMethod);
        assertSame(event, guiSocket.lastMouseEvent);
    }

    @Test
    void setInputSource_overwritesPreviousInputSource_forCaptureMouse() {
        TestInputSource source1 = new TestInputSource();
        TestInputSource source2 = new TestInputSource();

        MouseCaptureBehaviour behaviour = MouseCaptureBehaviour.buildWithoutDelegateSocket();

        behaviour.setInputRegister(source1);
        behaviour.setInputRegister(source2); // overwrite

        behaviour.captureMouse();

        assertEquals(0, source1.addMouseCount, "Old input source should not receive listeners");
        assertEquals(1, source2.addMouseCount, "New input source should receive the listener");
        assertNotNull(source2.lastAddedMouseListener);
    }

}