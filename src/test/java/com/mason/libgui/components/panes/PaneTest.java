package com.mason.libgui.components.panes;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.componentLayer.GUIMouseInputCoagulator;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

class PaneTest {

    private static class TestHitbox implements Hitbox{
        Coord coord = new Coord(0, 0);
        @Override public boolean withinBounds(Coord c) { return true; }
        @Override public void setCoord(Coord c) { coord = c; }
        @Override public Coord getCoord() { return coord; }
    }

    private static class TestComponent extends UIComponent{
        boolean renderCalled = false;
        Graphics2D lastGraphics;
        boolean tickCalled = false;

        TestComponent() {
            super(new TestHitbox());
        }

        @Override
        public void render(Graphics2D g) {
            renderCalled = true;
            lastGraphics = g;
        }

        @Override
        public void tick() {
            tickCalled = true;
        }
    }

    private static class TestBoundedMouseListener implements BoundedMouseInputListener{
        boolean within = true;
        String lastMethod;
        MouseInputEvent lastEvent;

        @Override public boolean withinBounds(Coord c) { return within; }

        @Override public void onMouseMoved(MouseInputEvent e) { lastMethod = "moved"; lastEvent = e; }
        @Override public void onMouseDragged(MouseInputEvent e) { lastMethod = "dragged"; lastEvent = e; }
        @Override public void onMousePressed(MouseInputEvent e) { lastMethod = "pressed"; lastEvent = e; }
        @Override public void onMouseReleased(MouseInputEvent e) { lastMethod = "released"; lastEvent = e; }
        @Override public void onMouseClicked(MouseInputEvent e) { lastMethod = "clicked"; lastEvent = e; }
        @Override public void onMouseWheel(MouseInputEvent e) { lastMethod = "wheel"; lastEvent = e; }
    }

    private static class TestGUIInputRegister implements GUIInputRegister<BoundedMouseInputListener>{
        KeyListener addedKey, removedKey;
        BoundedMouseInputListener addedMouse, removedMouse;

        @Override public void addMouseInputListener(BoundedMouseInputListener listener) { addedMouse = listener; }
        @Override public void removeMouseInputListener(BoundedMouseInputListener listener) { removedMouse = listener; }
        @Override public void addKeyListener(KeyListener listener) { addedKey = listener; }
        @Override public void removeKeyListener(KeyListener listener) { removedKey = listener; }
    }

    // ---------- Reflection helpers ----------

    private UIComponentManager getComponentManager(Pane pane) throws Exception {
        Field f = Pane.class.getDeclaredField("componentManager");
        f.setAccessible(true);
        return (UIComponentManager) f.get(pane);
    }

    private PaneGUIInputTranslator getInputTranslator(Pane pane) throws Exception {
        Field f = Pane.class.getDeclaredField("inputTranslator");
        f.setAccessible(true);
        return (PaneGUIInputTranslator) f.get(pane);
    }

    // ---------- Rendering / transform ----------

    @Test
    void render_appliesPaneTransformAndRendersChildComponents() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(10, 20), new Size(30, 40));
        Pane pane = new Pane(boundary);

        UIComponentManager mgr = getComponentManager(pane);
        TestComponent child = new TestComponent();
        pane.addComponent(child);

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            AffineTransform parentTx = parent.getTransform();
            assertEquals(new AffineTransform(), parentTx);

            pane.render(parent);

            assertTrue(child.renderCalled, "Child component should be rendered");

            Graphics2D childG = child.lastGraphics;
            assertNotNull(childG);

            // Expected transform: parent * translate(boundary.coord)
            AffineTransform expected = new AffineTransform(parentTx);
            expected.concatenate(AffineTransform.getTranslateInstance(10, 20));

            assertEquals(expected, childG.getTransform(), "Child should see translated transform");

            // Clip should be (0,0,width,height) of boundary
            Shape clip = childG.getClip();
            assertNotNull(clip);
            Rectangle bounds = clip.getBounds();
            assertEquals(0, bounds.x);
            assertEquals(0, bounds.y);
            assertEquals(boundary.getSize().width(), bounds.width);
            assertEquals(boundary.getSize().height(), bounds.height);

            // Parent unchanged
            assertEquals(parentTx, parent.getTransform());
            assertNull(parent.getClip());
        } finally {
            parent.dispose();
        }
    }

    // ---------- Ticking ----------

    @Test
    void tick_callsTickOnAllChildComponents() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);

        TestComponent c1 = new TestComponent();
        TestComponent c2 = new TestComponent();
        pane.addComponent(c1);
        pane.addComponent(c2);

        pane.tick();

        assertTrue(c1.tickCalled);
        assertTrue(c2.tickCalled);
    }

    @Test
    void removeComponent_stopsTickingRemovedComponent() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);

        TestComponent c1 = new TestComponent();
        TestComponent c2 = new TestComponent();
        pane.addComponent(c1);
        pane.addComponent(c2);

        pane.removeComponent(c1);

        pane.tick();

        assertFalse(c1.tickCalled, "Removed component should not be ticked");
        assertTrue(c2.tickCalled);
    }

    // ---------- Key listener delegation ----------

    private static class TestKeyListener implements KeyListener {
        KeyEvent lastTyped, lastPressed, lastReleased;
        @Override public void keyTyped(KeyEvent e) { lastTyped = e; }
        @Override public void keyPressed(KeyEvent e) { lastPressed = e; }
        @Override public void keyReleased(KeyEvent e) { lastReleased = e; }
    }

    @Test
    void addKeyListener_registersWithUnderlyingComponentManager() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);
        UIComponentManager mgr = getComponentManager(pane);

        TestKeyListener listener = new TestKeyListener();
        pane.addKeyListener(listener);

        KeyEvent evt = new KeyEvent(new java.awt.Button(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');

        mgr.getInputDistributor().addKeyListener(listener); // already added via Pane, but harmless
        mgr.getInputDistributor().keyPressed(evt);

        assertSame(evt, listener.lastPressed);
    }

    @Test
    void removeKeyListener_unregistersFromUnderlyingComponentManager() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);
        UIComponentManager mgr = getComponentManager(pane);

        TestKeyListener listener = new TestKeyListener();
        pane.addKeyListener(listener);
        pane.removeKeyListener(listener);

        KeyEvent evt = new KeyEvent(new java.awt.Button(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');

        mgr.getInputDistributor().keyPressed(evt);
        assertNull(listener.lastPressed, "Listener should not receive events after removal");
    }

    // ---------- Mouse listener delegation ----------

    @Test
    void addMouseInputListener_registersWithUnderlyingComponentManager() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);
        UIComponentManager mgr = getComponentManager(pane);

        TestBoundedMouseListener listener = new TestBoundedMouseListener();
        pane.addMouseInputListener(listener);

        // UIComponentManagerInputDistributor implements GUIMouseInputCoagulator
        GUIMouseInputCoagulator coagulator =
                (GUIMouseInputCoagulator) mgr.getInputDistributor();

        // Create a click event inside bounds
        MouseEvent raw = new MouseEvent(
                new java.awt.Button(),
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10, 10,
                1,
                false
        );
        MouseInputEvent e = new MouseInputEvent(raw);

        coagulator.onMouseInput(e);

        assertEquals("clicked", listener.lastMethod);
        assertSame(e, listener.lastEvent);
    }

    @Test
    void removeMouseInputListener_unregistersFromUnderlyingComponentManager() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(100, 100));
        Pane pane = new Pane(boundary);
        UIComponentManager mgr = getComponentManager(pane);

        TestBoundedMouseListener listener = new TestBoundedMouseListener();
        pane.addMouseInputListener(listener);
        pane.removeMouseInputListener(listener);

        GUIMouseInputCoagulator coagulator =
                (GUIMouseInputCoagulator) mgr.getInputDistributor();

        MouseEvent raw = new MouseEvent(
                new java.awt.Button(),
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10, 10,
                1,
                false
        );
        MouseInputEvent e = new MouseInputEvent(raw);

        coagulator.onMouseInput(e);

        assertNull(listener.lastMethod, "Removed listener should not receive mouse events");
    }

    // ---------- GUI input registration for PaneGUIInputTranslator ----------

    @Test
    void registerForGUIInput_registersInputTranslatorAsKeyAndMouseListener() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(50, 50));
        Pane pane = new Pane(boundary);

        PaneGUIInputTranslator translator = getInputTranslator(pane);

        TestGUIInputRegister inputRegister = new TestGUIInputRegister();

        pane.setInputSource(inputRegister);

        assertNotNull(inputRegister.addedKey);
        assertNotNull(inputRegister.addedMouse);

        assertSame(translator, inputRegister.addedKey);
        assertSame(translator, inputRegister.addedMouse);
    }

    @Test
    void deregisterForGUIInput_removesInputTranslatorFromKeyAndMouseListeners() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(50, 50));
        Pane pane = new Pane(boundary);

        PaneGUIInputTranslator translator = getInputTranslator(pane);

        TestGUIInputRegister inputRegister = new TestGUIInputRegister();

        pane.setInputSource(inputRegister);
        pane.unsetInputSource(inputRegister);

        assertSame(translator, inputRegister.removedKey);
        assertSame(translator, inputRegister.removedMouse);
    }

}