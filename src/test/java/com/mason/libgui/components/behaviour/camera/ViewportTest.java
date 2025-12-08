package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

class ViewportTest{

    private static final double EPS = 1e-9;

    // ---------- Helpers ----------

    private MouseInputEvent wheelEvent(int rotation) {
        MouseWheelEvent raw = new MouseWheelEvent(
                new JButton(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                0, 0,
                0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                rotation
        );
        return new MouseInputEvent(raw);
    }

    private HitboxRect getView(Viewport viewport) throws Exception {
        Field f = Viewport.class.getDeclaredField("view");
        f.setAccessible(true);
        return (HitboxRect) f.get(viewport);
    }

    // ---------- Construction / builders ----------

    @Test
    void buildViewport_initialisesViewFromInitialViewAndZoom() {
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(10, 20, 30, 40);
        Zoom zoom = Zoom.buildZoom(0.5, 4.0, 5, 2.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        assertEquals(new Coord(10, 20), viewport.getTopLeft());
        assertEquals(2.0, viewport.getZoom(), EPS);
    }

    @Test
    void buildViewportWithDefaultZoomAndInitialView_usesViewWithCoordsRelativeToPane() throws Exception {
        Rect bounding = new Rect(5, 7, 50, 60);
        Consumer<Graphics2D> renderable = g -> {};

        Viewport viewport = Viewport.buildViewportWithDefaultZoomAndInitialView(renderable, bounding);

        // View top-left should be at the boundary
        assertEquals(new Coord(0, 0), viewport.getTopLeft());

        // Zoom should be the same as default fully-zoomed-out zoom initial value (which is 1.0)
        Zoom defaultZoom = Zoom.buildDefaultFullyZoomedOutZoom();
        assertEquals(defaultZoom.getZoom(), viewport.getZoom(), EPS);
    }

    // ---------- Coordinate transforms ----------

    @Test
    void apparentToScreen_and_screenToApparent_areInversesForAlignedPoints() {
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(10, 20, 50, 50);

        // Choose zoom with initial value 2.0
        Zoom zoom = Zoom.buildZoom(1.0, 16.0, 5, 2.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        Coord apparent = new Coord(12, 22); // 2 units right/down from top-left

        Coord screen = viewport.apparentToScreen(apparent);
        assertEquals(new Coord(4, 4), screen); // (12-10)*2, (22-20)*2

        Coord back = viewport.screenToApparent(screen);
        assertEquals(apparent, back);
    }

    // ---------- Zoom delegation ----------

    @Test
    void mouseWheel_updatesZoomAndViewportReflectsIt() {
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(0, 0, 50, 50);
        Zoom zoom = Zoom.buildZoom(1.0, 16.0, 5, 4.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        double before = viewport.getZoom();
        assertEquals(4.0, before, EPS);

        viewport.mouseWheel(wheelEvent(-1)); // zoom in
        double afterViewport = viewport.getZoom();
        double afterZoom = zoom.getZoom();

        assertNotEquals(before, afterViewport);
        assertEquals(afterZoom, afterViewport, EPS);
    }

    // ---------- Resize after zoom ----------

    @Test
    void resizeAfterZoom_scalesViewSizeInverseToZoomFactorChange() throws Exception {
        Rect bounding = new Rect(0, 0, 200, 200);
        Rect initialView = new Rect(0, 0, 100, 80);
        Zoom zoom = Zoom.buildZoom(1.0, 4.0, 3, 1.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        HitboxRect view = getView(viewport);
        Size original = view.getSize();
        assertEquals(100, original.width());
        assertEquals(80, original.height());

        viewport.resizeAfterZoom(2.0); // halve width/height

        Size resized = view.getSize();
        assertEquals(50, resized.width());
        assertEquals(40, resized.height());
    }

    // ---------- Clamp within boundary ----------

    @Test
    void clampWithinBoundary_clampsViewTopLeftToBoundingRect() {
        Rect bounding = new Rect(0, 0, 100, 100);
        // initial view well outside bounding area
        Rect initialView = new Rect(200, 300, 10, 10);
        Zoom zoom = Zoom.buildZoom(1.0, 4.0, 3, 1.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        // Before clamp: top-left is outside
        assertEquals(new Coord(200, 300), viewport.getTopLeft());

        viewport.clampWithinBoundary();

        // HitboxRect.clampWithinBoundary clamps to boundaryX + width, boundaryY + height
        assertEquals(new Coord(90, 90), viewport.getTopLeft());
    }

    // ---------- Top-left getters/setters ----------

    @Test
    void setTopLeftAndGetTopLeft_updateViewPosition() {
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(0, 0, 50, 50);
        Zoom zoom = Zoom.buildZoom(1.0, 4.0, 3, 1.0);

        Consumer<Graphics2D> renderable = g -> {};
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        Coord newCoord = new Coord(30, 40);
        viewport.setTopLeft(newCoord);

        assertEquals(newCoord, viewport.getTopLeft());
    }

    // ---------- Render / transform behaviour ----------

    @Test
    void render_appliesTransformBasedOnViewCoordAndZoom() {
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(10, 20, 50, 50);
        Zoom zoom = Zoom.buildZoom(1.0, 16.0, 5, 2.0);

        AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();

        Consumer<Graphics2D> renderable = g -> seenTransform.set(g.getTransform());
        Viewport viewport = Viewport.buildViewport(renderable, bounding, initialView, zoom);

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            // Parent starts with identity transform
            AffineTransform parentTx = parent.getTransform();
            assertEquals(new AffineTransform(), parentTx);

            viewport.renderAfterTranslation(parent);

            AffineTransform childTx = seenTransform.get();
            assertNotNull(childTx);

            // Expected: translate(-view.x, -view.y) then scale(zoom, zoom)
            AffineTransform expected = new AffineTransform();
            expected.scale(2.0, 2.0);
            expected.translate(-10, -20);

            assertEquals(expected, childTx);

            // Parent should remain unchanged (GraphicsTransformBehaviour uses g.create())
            assertEquals(parentTx, parent.getTransform());
        } finally {
            parent.dispose();
        }
    }

}