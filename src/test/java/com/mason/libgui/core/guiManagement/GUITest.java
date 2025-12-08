package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.IterableUIComponentContainer;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.componentLayer.GUIInputDistributor;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import com.mason.libgui.core.input.rawLayer.RawToGUIInputAdapter;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

class GUITest{

    private static class TestHitbox implements Hitbox{
        Coord coord = new Coord(0, 0);
        @Override public boolean withinBounds(Coord c) { return true; }
        @Override public void setCoord(Coord c) { coord = c; }
        @Override public Coord getCoord() { return coord; }
    }

    private static class TestComponent extends UIComponent {
        boolean tickCalled = false;
        boolean renderCalled = false;

        TestComponent() {
            super(new TestHitbox());
        }

        @Override
        public void render(Graphics2D g) {
            renderCalled = true;
        }

        @Override
        public void tick() {
            tickCalled = true;
        }
    }

    private static class TestContainer implements IterableUIComponentContainer{
        final ArrayList<UIComponent> components = new ArrayList<>();

        @Override
        public Iterator<UIComponent> iterator() {
            return components.iterator();
        }

        @Override
        public void addComponent(UIComponent comp) {
            components.add(comp);
        }

        @Override
        public void removeComponent(UIComponent comp) {
            components.remove(comp);
        }
    }

    private static class DummyInputDistributor implements GUIInputDistributor<BoundedMouseInputListener>{
        @Override public void addMouseInputListener(BoundedMouseInputListener listener) {}
        @Override public void removeMouseInputListener(BoundedMouseInputListener listener) {}
        @Override public void addKeyListener(KeyListener listener) {}
        @Override public void removeKeyListener(KeyListener listener) {}
    }

    // RawInputSocket implementation to pass into GUI
    private static class TestRawInputSocket implements RawInputSocket{
        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}

        @Override public void mouseClicked(java.awt.event.MouseEvent e) {}
        @Override public void mousePressed(java.awt.event.MouseEvent e) {}
        @Override public void mouseReleased(java.awt.event.MouseEvent e) {}
        @Override public void mouseEntered(java.awt.event.MouseEvent e) {}
        @Override public void mouseExited(java.awt.event.MouseEvent e) {}

        @Override public void mouseDragged(java.awt.event.MouseEvent e) {}
        @Override public void mouseMoved(java.awt.event.MouseEvent e) {}

        @Override public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {}
    }

    // Window subclass to intercept registerInputSocket and render
    private static class TestWindow extends Window {

        boolean registerCalled = false;
        RawInputSocket lastSocket;

        int renderCalls = 0;
        UIComponentManager lastRenderManager;

        TestWindow(Size size, String title) {
            super(size, title);
        }

        @Override
        void registerInputSocket(RawInputSocket processor) {
            registerCalled = true;
            lastSocket = processor;
            // do NOT call super.registerInputSocket to avoid extra side-effects in tests
        }

        @Override
        void render(UIComponentManager compManager) {
            renderCalls++;
            lastRenderManager = compManager;
            // don't call super.render to avoid actual Canvas rendering in tests
        }
    }

    private JFrame getFrame(Window window) throws Exception {
        Field f = Window.class.getDeclaredField("frame");
        f.setAccessible(true);
        return (JFrame) f.get(window);
    }

    // ---------- Tests ----------

    @Test
    void buildUnwiredGUI_registersInputSocketWithWindow_andKeepsManager() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(200, 150);
        TestWindow window = new TestWindow(size, "Test");
        TestContainer container = new TestContainer();
        DummyInputDistributor distributor = new DummyInputDistributor();
        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        TestRawInputSocket socket = new TestRawInputSocket();

        GUI gui = GUI.buildUnwiredGUI(window, manager, socket);

        // Window.registerInputSocket should have been called with our socket
        assertTrue(window.registerCalled);
        assertSame(socket, window.lastSocket);

        // Add a component via GUI; it should go into the container via UIComponentManager
        TestComponent c = new TestComponent();
        gui.addComponent(c);
        assertTrue(container.components.contains(c));

        gui.removeComponent(c);
        assertFalse(container.components.contains(c));

        // Clean up the real frame created in TestWindow
        getFrame(window).dispose();
    }

    @Test
    void tick_callsComponentManagerTickComponents() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(200, 150);
        TestWindow window = new TestWindow(size, "Tick Test");
        TestContainer container = new TestContainer();
        DummyInputDistributor distributor = new DummyInputDistributor();
        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        TestRawInputSocket socket = new TestRawInputSocket();
        GUI gui = GUI.buildUnwiredGUI(window, manager, socket);

        // Add a component that records tick()
        TestComponent c = new TestComponent();
        gui.addComponent(c);

        // Invoke private tick() via reflection
        Method tickMethod = GUI.class.getDeclaredMethod("tick");
        tickMethod.setAccessible(true);
        tickMethod.invoke(gui);

        assertTrue(c.tickCalled);

        getFrame(window).dispose();
    }

    @Test
    void render_callsWindowRenderWithManager() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(200, 150);
        TestWindow window = new TestWindow(size, "Render Test");
        TestContainer container = new TestContainer();
        DummyInputDistributor distributor = new DummyInputDistributor();
        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        TestRawInputSocket socket = new TestRawInputSocket();
        GUI gui = GUI.buildUnwiredGUI(window, manager, socket);

        // Call private render() via reflection
        Method renderMethod = GUI.class.getDeclaredMethod("render");
        renderMethod.setAccessible(true);
        renderMethod.invoke(gui);

        assertEquals(1, window.renderCalls);
        assertSame(manager, window.lastRenderManager);

        getFrame(window).dispose();
    }

    @Test
    void buildSimpleGUI_createsGuiWithRawToGUIInputAdapter() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(100, 80);
        GUI gui = GUI.buildSimpleGUI(size, "Simple GUI");
        assertNotNull(gui);

        // Check that the inputSocket field is a RawToGUIInputAdapter
        Field f = GUI.class.getDeclaredField("inputSocket");
        f.setAccessible(true);
        Object socket = f.get(gui);

        assertTrue(socket instanceof RawToGUIInputAdapter);

        // No need to call start(); that would spin up a Pacemaker thread & real rendering.
        // Just ensure we can terminate without throwing.
        gui.terminate();

        // Dispose the internal Window frame to avoid leaking test windows
        Field wField = GUI.class.getDeclaredField("window");
        wField.setAccessible(true);
        Window window = (Window) wField.get(gui);
        getFrame(window).dispose();
    }

}