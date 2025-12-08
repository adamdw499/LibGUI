package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.GraphicsTransformBehaviour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

class PaneGraphicsTransformBuilderTest {

    @Test
    void build_appliesTranslateTransformFromBoundaryCoord_andClipFromBoundarySize() {
        Coord topLeft = new Coord(10, 20);
        Size size = new Size(30, 40);
        RectQuery boundary = Rect.buildRect(topLeft, size);

        AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();
        AtomicReference<Shape> seenClip = new AtomicReference<>();

        Consumer<Graphics2D> renderable = g -> {
            seenTransform.set(g.getTransform());
            seenClip.set(g.getClip());
        };

        PaneGraphicsTransformBuilder builder =
                new PaneGraphicsTransformBuilder(renderable, boundary);
        GraphicsTransformBehaviour behaviour = builder.build();

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            // parent starts identity / null clip
            AffineTransform parentTx = parent.getTransform();
            Shape parentClip = parent.getClip();

            behaviour.transformAndRender(parent);

            // Transform on child should be translation by boundary coord
            AffineTransform expected = AffineTransform.getTranslateInstance(topLeft.x(), topLeft.y());
            assertEquals(expected, seenTransform.get());

            // Clip on child should be rectangle (0,0,width,height)
            Shape clip = seenClip.get();
            assertNotNull(clip);
            Rectangle2D bounds = clip.getBounds2D();
            assertEquals(0, bounds.getX(), 1e-9);
            assertEquals(0, bounds.getY(), 1e-9);
            assertEquals(size.width(), bounds.getWidth(), 1e-9);
            assertEquals(size.height(), bounds.getHeight(), 1e-9);

            // Parent graphics should be unchanged
            assertEquals(parentTx, parent.getTransform());
            assertEquals(parentClip, parent.getClip());
        } finally {
            parent.dispose();
        }
    }

    @Test
    void build_usesProvidedRenderable() {
        RectQuery boundary = Rect.buildRect(new Coord(0, 0), new Size(10, 10));
        AtomicBoolean called = new AtomicBoolean(false);

        Consumer<Graphics2D> renderable = g -> called.set(true);

        PaneGraphicsTransformBuilder builder =
                new PaneGraphicsTransformBuilder(renderable, boundary);
        GraphicsTransformBehaviour behaviour = builder.build();

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        try {
            behaviour.transformAndRender(g2);
        } finally {
            g2.dispose();
        }

        assertTrue(called.get(), "Renderable must be invoked by built GraphicsTransformBehaviour");
    }

    @Test
    void build_respectsNonIdentityParentTransform() {
        // Ensure the supplied translation is applied on top of whatever parent has
        Coord topLeft = new Coord(5, 7);
        Size size = new Size(20, 20);
        RectQuery boundary = Rect.buildRect(topLeft, size);

        AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();

        Consumer<Graphics2D> renderable = g -> seenTransform.set(g.getTransform());

        PaneGraphicsTransformBuilder builder =
                new PaneGraphicsTransformBuilder(renderable, boundary);
        GraphicsTransformBehaviour behaviour = builder.build();

        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            // Give parent a non-identity transform (scale)
            parent.scale(2.0, 3.0);
            AffineTransform parentTx = parent.getTransform();

            behaviour.transformAndRender(parent);

            // Expected: parentTx * translate(topLeft.x, topLeft.y)
            AffineTransform expected = new AffineTransform(parentTx);
            expected.concatenate(AffineTransform.getTranslateInstance(topLeft.x(), topLeft.y()));

            assertEquals(expected, seenTransform.get());
            // Parent unchanged
            assertEquals(parentTx, parent.getTransform());
        } finally {
            parent.dispose();
        }
    }

}