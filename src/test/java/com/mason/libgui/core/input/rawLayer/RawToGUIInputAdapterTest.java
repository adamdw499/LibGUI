package com.mason.libgui.core.input.rawLayer;

import com.mason.libgui.core.input.guiLayer.GUIInputSocket;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class RawToGUIInputAdapterTest{


    private static class TestGUIInputSocket implements GUIInputSocket{

        KeyEvent lastKeyTyped;
        KeyEvent lastKeyPressed;
        KeyEvent lastKeyReleased;

        String lastMouseMethod;
        MouseInputEvent lastMouseInputEvent;

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
        public void onMouseClicked(MouseInputEvent e) {
            lastMouseMethod = "clicked";
            lastMouseInputEvent = e;
        }

        @Override
        public void onMousePressed(MouseInputEvent e) {
            lastMouseMethod = "pressed";
            lastMouseInputEvent = e;
        }

        @Override
        public void onMouseReleased(MouseInputEvent e) {
            lastMouseMethod = "released";
            lastMouseInputEvent = e;
        }

        @Override
        public void onMouseDragged(MouseInputEvent e) {
            lastMouseMethod = "dragged";
            lastMouseInputEvent = e;
        }

        @Override
        public void onMouseMoved(MouseInputEvent e) {
            lastMouseMethod = "moved";
            lastMouseInputEvent = e;
        }

        @Override
        public void onMouseWheel(MouseInputEvent e) {
            lastMouseMethod = "wheel";
            lastMouseInputEvent = e;
        }
    }

    private RawToGUIInputAdapter createAdapterWithTestSocket(TestGUIInputSocket socket) {
        RawToGUIInputAdapter adapter = new RawToGUIInputAdapter();
        adapter.setGUIInputSocket(socket);
        return adapter;
    }

    // ---------- Key events ----------

    @Test
    void keyTyped_isDelegatedToGuiSocket() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_TYPED, System.currentTimeMillis(),
                0, KeyEvent.VK_UNDEFINED, 'a'
        );

        adapter.keyTyped(event);

        assertSame(event, gui.lastKeyTyped);
        assertNull(gui.lastKeyPressed);
        assertNull(gui.lastKeyReleased);
    }

    @Test
    void keyPressed_isDelegatedToGuiSocket() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                0, KeyEvent.VK_A, 'a'
        );

        adapter.keyPressed(event);

        assertSame(event, gui.lastKeyPressed);
        assertNull(gui.lastKeyTyped);
        assertNull(gui.lastKeyReleased);
    }

    @Test
    void keyReleased_isDelegatedToGuiSocket() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        KeyEvent event = new KeyEvent(
                new JButton(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
                0, KeyEvent.VK_A, 'a'
        );

        adapter.keyReleased(event);

        assertSame(event, gui.lastKeyReleased);
        assertNull(gui.lastKeyTyped);
        assertNull(gui.lastKeyPressed);
    }

    // ---------- Mouse button events ----------

    @Test
    void mouseClicked_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseEvent event = new MouseEvent(
                new JButton(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                0, 10, 20, 1, false
        );

        adapter.mouseClicked(event);

        assertEquals("clicked", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

    @Test
    void mousePressed_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseEvent event = new MouseEvent(
                new JButton(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(),
                0, 10, 20, 1, false
        );

        adapter.mousePressed(event);

        assertEquals("pressed", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

    @Test
    void mouseReleased_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseEvent event = new MouseEvent(
                new JButton(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(),
                0, 10, 20, 1, false
        );

        adapter.mouseReleased(event);

        assertEquals("released", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

    // ---------- Mouse motion events ----------

    @Test
    void mouseDragged_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseEvent event = new MouseEvent(
                new JButton(), MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(),
                0, 15, 25, 0, false
        );

        adapter.mouseDragged(event);

        assertEquals("dragged", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

    @Test
    void mouseMoved_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseEvent event = new MouseEvent(
                new JButton(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                0, 15, 25, 0, false
        );

        adapter.mouseMoved(event);

        assertEquals("moved", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

    // ---------- Mouse wheel ----------

    @Test
    void mouseWheelMoved_isAdaptedToMouseInputEventAndDelegated() {
        TestGUIInputSocket gui = new TestGUIInputSocket();
        RawToGUIInputAdapter adapter = createAdapterWithTestSocket(gui);

        MouseWheelEvent event = new MouseWheelEvent(
                new JButton(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                10, 20,
                0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                1
        );

        adapter.mouseWheelMoved(event);

        assertEquals("wheel", gui.lastMouseMethod);
        assertNotNull(gui.lastMouseInputEvent);
    }

}