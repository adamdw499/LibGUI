package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.camera.PanZoomBehaviour;
import com.mason.libgui.components.behaviour.camera.Viewport;
import com.mason.libgui.components.behaviour.camera.Zoom;
import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

class PanZoomPaneTest{

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

    // ---------- GUIInputRegister test double ----------

    private static class TestGUIInputRegister implements GUIInputRegister<BoundedMouseInputListener>{
        final ArrayList<KeyListener> addedKeys = new ArrayList<>();
        final ArrayList<KeyListener> removedKeys = new ArrayList<>();
        final ArrayList<BoundedMouseInputListener> addedMouse = new ArrayList<>();
        final ArrayList<BoundedMouseInputListener> removedMouse = new ArrayList<>();

        @Override public void addMouseInputListener(BoundedMouseInputListener listener) {
            addedMouse.add(listener);
        }
        @Override public void removeMouseInputListener(BoundedMouseInputListener listener) {
            removedMouse.add(listener);
        }
        @Override public void addKeyListener(KeyListener listener) {
            addedKeys.add(listener);
        }
        @Override public void removeKeyListener(KeyListener listener) {
            removedKeys.add(listener);
        }
    }

    // ---------- Reflection helpers ----------

    private PanZoomBehaviour getPanZoomBehaviour(PanZoomPane pane) throws Exception {
        Field f = PanZoomPane.class.getDeclaredField("panZoomBehaviour");
        f.setAccessible(true);
        return (PanZoomBehaviour) f.get(pane);
    }

    private Viewport getViewport(PanZoomBehaviour behaviour) throws Exception {
        Field f = PanZoomBehaviour.class.getDeclaredField("viewport");
        f.setAccessible(true);
        return (Viewport) f.get(behaviour);
    }

    private PaneGUIInputTranslator getInputTranslator(Pane pane) throws Exception {
        Field f = Pane.class.getDeclaredField("inputTranslator");
        f.setAccessible(true);
        return (PaneGUIInputTranslator) f.get(pane);
    }

    private HitboxRect getViewportView(Viewport viewport) throws Exception {
        Field f = Viewport.class.getDeclaredField("view");
        f.setAccessible(true);
        return (HitboxRect) f.get(viewport);
    }

    private Zoom getViewportZoom(Viewport viewport) throws Exception {
        Field f = Viewport.class.getDeclaredField("zoom");
        f.setAccessible(true);
        return (Zoom) f.get(viewport);
    }

    private double getZoomValue(Zoom zoom) throws Exception {
        Field f = Zoom.class.getDeclaredField("zoom");
        f.setAccessible(true);
        return f.getDouble(zoom);
    }

    private double getMinZoom(Zoom zoom) throws Exception {
        Field f = Zoom.class.getDeclaredField("minZoom");
        f.setAccessible(true);
        return f.getDouble(zoom);
    }

    private double getMaxZoom(Zoom zoom) throws Exception {
        Field f = Zoom.class.getDeclaredField("maxZoom");
        f.setAccessible(true);
        return f.getDouble(zoom);
    }

    private Zoom newZoom(double minZoom, double maxZoom, double zoomFactor, double initialZoom) throws Exception {
        Constructor<Zoom> ctor =
                Zoom.class.getDeclaredConstructor(double.class, double.class, double.class, double.class);
        ctor.setAccessible(true);
        return ctor.newInstance(minZoom, maxZoom, zoomFactor, initialZoom);
    }

    // ---------- Builder behaviour ----------

    @Test
    void build_configuresViewportWithInitialViewAndZoom() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(5, 7), new Size(100, 80));
        Rect initialView = new Rect(10, 20, 30, 40);

        // Create a Zoom via reflection (private ctor)
        Zoom zoom = newZoom(1.0, 16.0, 2.0, 2.5); // initial zoom 2.5

        PanZoomPaneBuilder<PanZoomPane> builder = new PanZoomPaneBuilder<>(PanZoomPane::new);
        PanZoomPane pane = builder.build(boundary, initialView, zoom);

        PanZoomBehaviour behaviour = getPanZoomBehaviour(pane);
        Viewport viewport = getViewport(behaviour);
        HitboxRect view = getViewportView(viewport);
        Zoom vpZoom = getViewportZoom(viewport);

        assertEquals(initialView.getCoord(), view.getCoord());
        assertEquals(2.5, getZoomValue(vpZoom), 1e-9);
    }

    @Test
    void buildFullyZoomedOutPane_usesBoundaryAsInitialViewAndDefaultZoomFields() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(200, 150));
        PanZoomPaneBuilder<PanZoomPane> builder = new PanZoomPaneBuilder<>(PanZoomPane::new);
        PanZoomPane pane = builder.buildFullyZoomedOutPane(boundary);

        PanZoomBehaviour behaviour = getPanZoomBehaviour(pane);
        Viewport viewport = getViewport(behaviour);
        HitboxRect view = getViewportView(viewport);
        Zoom vpZoom = getViewportZoom(viewport);

        // Viewport's view should start at boundary coord
        assertEquals(boundary.getCoord(), view.getCoord());

        // For the default fully zoomed-out behaviour, internal Zoom should have:
        // minZoom = 1, maxZoom = 16*16 = 256, initial zoom = 1
        assertEquals(1.0, getMinZoom(vpZoom), 1e-9);
        assertEquals(256.0, getMaxZoom(vpZoom), 1e-9);
        assertEquals(1.0, getZoomValue(vpZoom), 1e-9);
    }

    // ---------- Render behaviour ----------

    @Test
    void render_usesPanAndZoomAndUltimatelyRendersChildComponents() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(10, 20), new Size(100, 80));

        PanZoomPaneBuilder<PanZoomPane> builder = new PanZoomPaneBuilder<>(PanZoomPane::new);
        PanZoomPane pane = builder.buildFullyZoomedOutPane(boundary);

        TestComponent child = new TestComponent();
        pane.addComponent(child);

        BufferedImage img = new BufferedImage(300, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            AffineTransform parentTx = parent.getTransform();

            pane.render(parent);

            assertTrue(child.renderCalled, "Child component should be rendered via PanAndZoomPane.render()");

            // Child should have seen some non-identity transform (Pan+Pane)
            AffineTransform childTx = child.lastGraphics.getTransform();
            assertNotNull(childTx);
            assertNotEquals(parentTx, childTx, "Transform applied to child should differ from parent");

            // Parent transform must remain unchanged
            assertEquals(parentTx, parent.getTransform());
        } finally {
            parent.dispose();
        }
    }

    // ---------- GUI input registration ----------

    @Test
    void registerForGUIInput_registersPaneTranslatorAndPanZoomBehaviour() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(50, 50));
        PanZoomPaneBuilder<PanZoomPane> builder = new PanZoomPaneBuilder<>(PanZoomPane::new);
        PanZoomPane pane = builder.buildFullyZoomedOutPane(boundary);

        PaneGUIInputTranslator translator = getInputTranslator(pane);

        TestGUIInputRegister reg = new TestGUIInputRegister();

        pane.setInputSource(reg);

        // Key listener: only the translator
        assertEquals(1, reg.addedKeys.size());
        assertSame(translator, reg.addedKeys.get(0));

        // Mouse listeners: translator and panAndZoomBehaviour
        assertTrue(reg.addedMouse.contains(translator),
                "PaneGUIInputTranslator should be registered as mouse listener");
    }

    @Test
    void deregisterForGUIInput_removesPaneTranslatorAndPanZoomBehaviour() throws Exception {
        HitboxRect boundary = new HitboxRect(new Coord(0, 0), new Size(50, 50));
        PanZoomPaneBuilder<PanZoomPane> builder = new PanZoomPaneBuilder<>(PanZoomPane::new);
        PanZoomPane pane = builder.buildFullyZoomedOutPane(boundary);

        PaneGUIInputTranslator translator = getInputTranslator(pane);
        PanZoomBehaviour panAndZoom = getPanZoomBehaviour(pane);

        TestGUIInputRegister reg = new TestGUIInputRegister();

        pane.setInputSource(reg);
        pane.unsetInputSource(reg);

        // Translator removed as key and mouse listener
        assertTrue(reg.removedKeys.contains(translator),
                "Translator should be removed as key listener");
        assertTrue(reg.removedMouse.contains(translator),
                "Translator should be removed as mouse listener");
    }

}