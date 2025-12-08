package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class CanvasRendererTest{

    private static class TestRawInputSocket implements RawInputSocket {
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

    // Simple Hitbox + UIComponent for render testing
    private static class TestHitbox implements Hitbox {
        Coord coord = new Coord(0, 0);
        @Override public boolean withinBounds(Coord c) { return true; }
        @Override public void setCoord(Coord c) { coord = c; }
        @Override public Coord getCoord() { return coord; }
    }

    private static class TestComponent extends UIComponent {
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

    // Fake BufferStrategy backed by a BufferedImage
    private static class TestBufferStrategy extends BufferStrategy {
        final BufferedImage image;
        boolean showCalled = false;

        TestBufferStrategy(int w, int h) {
            this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public BufferCapabilities getCapabilities(){
            return null;
        }

        @Override
        public Graphics getDrawGraphics() {
            // Each caller disposes its own Graphics, so create a fresh one
            return image.createGraphics();
        }

        @Override
        public boolean contentsLost() {
            return false;
        }

        @Override
        public boolean contentsRestored() {
            return false;
        }

        @Override
        public void show() {
            showCalled = true;
        }
    }

    // Canvas that records createBufferStrategy calls and/or returns a predefined strategy
    private static class TestCanvas extends Canvas {
        boolean createCalled = false;
        int createNumBuffers = -1;
        BufferStrategy strategy;

        TestCanvas(BufferStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void createBufferStrategy(int numBuffers) {
            createCalled = true;
            createNumBuffers = numBuffers;
            // No real buffer strategy creation to avoid peer issues
        }

        @Override
        public BufferStrategy getBufferStrategy() {
            return strategy;
        }
    }

    private Canvas getCanvas(CanvasRenderer renderer) throws Exception {
        Field f = CanvasRenderer.class.getDeclaredField("canvas");
        f.setAccessible(true);
        return (Canvas) f.get(renderer);
    }

    private void setCanvas(CanvasRenderer renderer, Canvas newCanvas) throws Exception {
        Field f = CanvasRenderer.class.getDeclaredField("canvas");
        f.setAccessible(true);
        f.set(renderer, newCanvas);
    }

    private Size getFrameSize(CanvasRenderer renderer) throws Exception {
        Field f = CanvasRenderer.class.getDeclaredField("frameSize");
        f.setAccessible(true);
        return (Size) f.get(renderer);
    }

    // ---------- Constructor ----------

    @Test
    void constructor_setsPreferredSize_andNotFocusable_andStoresFrameSize() throws Exception {
        Size size = new Size(800, 600);
        CanvasRenderer renderer = new CanvasRenderer(size);

        Canvas canvas = getCanvas(renderer);
        Dimension pref = canvas.getPreferredSize();

        assertEquals(800, pref.width);
        assertEquals(600, pref.height);
        assertFalse(canvas.isFocusable());

        Size stored = getFrameSize(renderer);
        assertEquals(size.width(), stored.width());
        assertEquals(size.height(), stored.height());
    }

    // ---------- setUpWithFrame ----------

    @Test
    void setUpWithFrame_addsCanvasToFrame_andCallsCreateBufferStrategy() throws Exception {
        Size size = new Size(200, 150);
        CanvasRenderer renderer = new CanvasRenderer(size);

        // Replace the internal canvas with our test canvas to avoid real peer issues
        TestCanvas testCanvas = new TestCanvas(null);
        setCanvas(renderer, testCanvas);

        JFrame frame = new JFrame();
        try {
            renderer.setUpWithFrame(frame);

            // Canvas added?
            boolean found = false;
            for (Component c : frame.getContentPane().getComponents()) {
                if (c == testCanvas) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Canvas should be added to frame");

            // Buffer strategy creation invoked?
            assertTrue(testCanvas.createCalled, "createBufferStrategy should be called");
            assertEquals(4, testCanvas.createNumBuffers);
        } finally {
            frame.dispose();
        }
    }

    // ---------- registerInputSocket ----------

    @Test
    void registerInputSocket_registersMouseListenersOnCanvas() throws Exception {
        Size size = new Size(100, 100);
        CanvasRenderer renderer = new CanvasRenderer(size);
        Canvas canvas = getCanvas(renderer);

        TestRawInputSocket socket = new TestRawInputSocket();
        renderer.registerInputSocket(socket);

        assertTrue(contains(canvas.getMouseListeners(), socket),
                "Socket should be registered as MouseListener");
        assertTrue(contains(canvas.getMouseMotionListeners(), socket),
                "Socket should be registered as MouseMotionListener");
        assertTrue(contains(canvas.getMouseWheelListeners(), socket),
                "Socket should be registered as MouseWheelListener");
    }

    private boolean contains(Object[] arr, Object needle) {
        for (Object o : arr) {
            if (o == needle) return true;
        }
        return false;
    }

    // ---------- paintBackground (via reflection + image) ----------

    @Test
    void paintBackground_fillsWithBackgroundColorOverFrameSize() throws Exception {
        Size size = new Size(50, 40);
        CanvasRenderer renderer = new CanvasRenderer(size);

        Method paintBackground = CanvasRenderer.class
                .getDeclaredMethod("paintBackground", Graphics2D.class);
        paintBackground.setAccessible(true);

        BufferedImage img = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            paintBackground.invoke(renderer, g);
        } finally {
            g.dispose();
        }

        int expected = CanvasRenderer.BACKGROUND_COLOR.getRGB();
        assertEquals(expected, img.getRGB(0, 0));
        assertEquals(expected, img.getRGB(size.width() - 1, 0));
        assertEquals(expected, img.getRGB(0, size.height() - 1));
        assertEquals(expected, img.getRGB(size.width() - 1, size.height() - 1));
    }

    // ---------- render ----------

    @Test
    void render_paintsBackground_andCallsRenderComponents() throws Exception {
        Size size = new Size(60, 40);
        CanvasRenderer renderer = new CanvasRenderer(size);

        // Prepare fake buffer strategy and canvas
        TestBufferStrategy strategy = new TestBufferStrategy(size.width(), size.height());
        TestCanvas testCanvas = new TestCanvas(strategy);
        setCanvas(renderer, testCanvas);

        // UIComponentManager with a test component
        UIComponentManager manager = UIComponentManager.buildSimpleUIComponentManager();
        TestComponent comp = new TestComponent();
        manager.addComponent(comp);

        // Call render: should draw background, then component
        renderer.render(manager);

        assertTrue(comp.renderCalled, "Component's render should be called");
        assertTrue(strategy.showCalled, "BufferStrategy.show() should be called");

        // Check that background was painted
        int bg = CanvasRenderer.BACKGROUND_COLOR.getRGB();
        assertEquals(bg, strategy.image.getRGB(0, 0));
        assertEquals(bg, strategy.image.getRGB(size.width() - 1, size.height() - 1));
    }

}