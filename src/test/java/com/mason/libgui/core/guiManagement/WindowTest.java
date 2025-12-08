package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

class WindowTest{

    private static class TestRawInputSocket implements RawInputSocket{

        @Override public void keyTyped(java.awt.event.KeyEvent e) {}
        @Override public void keyPressed(java.awt.event.KeyEvent e) {}
        @Override public void keyReleased(java.awt.event.KeyEvent e) {}

        @Override public void mouseClicked(java.awt.event.MouseEvent e) {}
        @Override public void mousePressed(java.awt.event.MouseEvent e) {}
        @Override public void mouseReleased(java.awt.event.MouseEvent e) {}
        @Override public void mouseEntered(java.awt.event.MouseEvent e) {}
        @Override public void mouseExited(java.awt.event.MouseEvent e) {}

        @Override public void mouseDragged(java.awt.event.MouseEvent e) {}
        @Override public void mouseMoved(java.awt.event.MouseEvent e) {}

        @Override public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {}
    }

    private static class TestHitbox implements Hitbox{
        Coord coord = new Coord(0, 0);
        @Override public boolean withinBounds(Coord c) { return true; }
        @Override public void setCoord(Coord c) { coord = c; }
        @Override public Coord getCoord() { return coord; }
    }

    private static class TestComponent extends UIComponent{
        boolean renderCalled = false;
        Graphics2D lastGraphics;

        TestComponent() {
            super(new TestHitbox());
        }

        @Override
        public void render(Graphics2D g) {
            renderCalled = true;
            lastGraphics = g;
        }

        @Override
        public void tick() {}
    }

    // ---------- Reflection helpers ----------

    private JFrame getFrame(Window window) throws Exception {
        Field f = Window.class.getDeclaredField("frame");
        f.setAccessible(true);
        return (JFrame) f.get(window);
    }

    private CanvasRenderer getRenderer(Window window) throws Exception {
        Field f = Window.class.getDeclaredField("renderer");
        f.setAccessible(true);
        return (CanvasRenderer) f.get(window);
    }

    private Canvas getCanvas(CanvasRenderer renderer) throws Exception {
        Field f = CanvasRenderer.class.getDeclaredField("canvas");
        f.setAccessible(true);
        return (Canvas) f.get(renderer);
    }

    // ---------- Tests ----------

    @Test
    void constructor_setsUpFrameAndCanvas() throws Exception {
        // Skip in headless environments
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(800, 600);
        Window window = new Window(size, "My Title");
        JFrame frame = getFrame(window);
        try {
            // Frame basic properties
            assertEquals("My Title", frame.getTitle());
            assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
            assertFalse(frame.isResizable());
            assertTrue(frame.isVisible());

            // Canvas should be added to the frame
            CanvasRenderer renderer = getRenderer(window);
            Canvas canvas = getCanvas(renderer);

            boolean foundCanvas = false;
            for (Component c : frame.getContentPane().getComponents()) {
                if (c == canvas) {
                    foundCanvas = true;
                    break;
                }
            }
            assertTrue(foundCanvas, "Canvas should be added to frame");

            // Preferred size of canvas matches frameSize
            Dimension pref = canvas.getPreferredSize();
            assertEquals(size.width(), pref.width);
            assertEquals(size.height(), pref.height);
        } finally {
            frame.dispose();
        }
    }

    @Test
    void registerInputSocket_registersKeyAndMouseListeners() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(400, 300);
        Window window = new Window(size, "Listeners Test");
        JFrame frame = getFrame(window);
        CanvasRenderer renderer = getRenderer(window);
        Canvas canvas = getCanvas(renderer);

        try {
            TestRawInputSocket socket = new TestRawInputSocket();
            window.registerInputSocket(socket);

            // Key listener on frame
            assertTrue(contains(frame.getKeyListeners(), socket),
                    "Socket should be registered as key listener on frame");

            // Mouse listeners on canvas
            assertTrue(contains(canvas.getMouseListeners(), socket),
                    "Socket should be registered as MouseListener on canvas");
            assertTrue(contains(canvas.getMouseMotionListeners(), socket),
                    "Socket should be registered as MouseMotionListener on canvas");
            assertTrue(contains(canvas.getMouseWheelListeners(), socket),
                    "Socket should be registered as MouseWheelListener on canvas");
        } finally {
            frame.dispose();
        }
    }

    private boolean contains(Object[] arr, Object needle) {
        for (Object o : arr) {
            if (o == needle) return true;
        }
        return false;
    }

    @Test
    void setToolbarImage_setsFrameIconImage() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(200, 150);
        Window window = new Window(size, "Icon Test");
        JFrame frame = getFrame(window);
        try {
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

            window.setToolbarImage(img);

            assertSame(img, frame.getIconImage());
        } finally {
            frame.dispose();
        }
    }

    @Test
    void setCursor_setsFrameCursor() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(200, 150);
        Window window = new Window(size, "Cursor Test");
        JFrame frame = getFrame(window);
        try {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

            window.setCursor(cursor);

            assertSame(cursor, frame.getCursor());
        } finally {
            frame.dispose();
        }
    }

    @Test
    void render_delegatesToRendererAndRendersComponents() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

        Size size = new Size(300, 200);
        Window window = new Window(size, "Render Test");
        JFrame frame = getFrame(window);
        try {
            UIComponentManager manager = UIComponentManager.buildSimpleUIComponentManager();
            TestComponent component = new TestComponent();
            manager.addComponent(component);

            // Call render once; CanvasRenderer should render background + component
            window.render(manager);

            assertTrue(component.renderCalled, "Component's render should be called via Window.render()");
        } finally {
            frame.dispose();
        }
    }

}