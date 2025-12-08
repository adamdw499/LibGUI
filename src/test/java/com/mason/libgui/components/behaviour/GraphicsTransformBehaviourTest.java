package com.mason.libgui.components.behaviour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

class GraphicsTransformBehaviourTest{

    @Test
    void render_appliesTransformAndClipBeforeRenderable_andLeavesParentUnchanged() {
        BufferedImage img = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            AffineTransform originalTransform = parent.getTransform();
            Shape originalClip = parent.getClip(); // likely null

            AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();
            AtomicReference<Shape> seenClip = new AtomicReference<>();

            Supplier<AffineTransform> transformSupplier =
                    () -> AffineTransform.getTranslateInstance(5, 7);
            Shape clipShape = new Rectangle(10, 20, 30, 40);
            Supplier<Shape> clipSupplier = () -> clipShape;

            Consumer<Graphics2D> renderable = g -> {
                seenTransform.set(g.getTransform());
                seenClip.set(g.getClip());
            };

            GraphicsTransformBehaviour behaviour =
                    GraphicsTransformBehaviour.buildBehaviour(renderable, transformSupplier, clipSupplier);

            behaviour.transformAndRender(parent);

            // Transform should be original * supplied
            AffineTransform expected = new AffineTransform(originalTransform);
            expected.concatenate(transformSupplier.get());
            assertEquals(expected, seenTransform.get());

            // Clip should be applied
            assertNotNull(seenClip.get());
            Rectangle2D expectedBounds = clipShape.getBounds2D();
            Rectangle2D actualBounds = seenClip.get().getBounds2D();
            assertEquals(expectedBounds, actualBounds);

            // Parent graphics should be unchanged
            assertEquals(originalTransform, parent.getTransform());
            assertEquals(originalClip, parent.getClip());
        } finally {
            parent.dispose();
        }
    }

    @Test
    void buildTransformOnlyBehaviour_appliesOnlyTransform_notClip() {
        BufferedImage img = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            AffineTransform originalTransform = parent.getTransform();
            Shape originalClip = parent.getClip();

            AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();
            AtomicReference<Shape> seenClip = new AtomicReference<>();

            Supplier<AffineTransform> transformSupplier =
                    () -> AffineTransform.getScaleInstance(2.0, 3.0);

            Consumer<Graphics2D> renderable = g -> {
                seenTransform.set(g.getTransform());
                seenClip.set(g.getClip());
            };

            GraphicsTransformBehaviour behaviour =
                    GraphicsTransformBehaviour.buildTransformOnlyBehaviour(renderable, transformSupplier);

            behaviour.transformAndRender(parent);

            AffineTransform expected = new AffineTransform(originalTransform);
            expected.concatenate(transformSupplier.get());
            assertEquals(expected, seenTransform.get());

            // Clip should be unchanged (typically null)
            assertEquals(originalClip, seenClip.get());

            // Parent unchanged
            assertEquals(originalTransform, parent.getTransform());
            assertEquals(originalClip, parent.getClip());
        } finally {
            parent.dispose();
        }
    }

    @Test
    void buildClipOnlyBehaviour_appliesOnlyClip_notTransform() {
        BufferedImage img = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            AffineTransform originalTransform = parent.getTransform();
            Shape originalClip = parent.getClip();

            AtomicReference<AffineTransform> seenTransform = new AtomicReference<>();
            AtomicReference<Shape> seenClip = new AtomicReference<>();

            Shape clipShape = new Rectangle(5, 6, 20, 15);
            Supplier<Shape> clipSupplier = () -> clipShape;

            Consumer<Graphics2D> renderable = g -> {
                seenTransform.set(g.getTransform());
                seenClip.set(g.getClip());
            };

            GraphicsTransformBehaviour behaviour =
                    GraphicsTransformBehaviour.buildClipOnlyBehaviour(renderable, clipSupplier);

            behaviour.transformAndRender(parent);

            // Transform should be unchanged
            assertEquals(originalTransform, seenTransform.get());

            // Clip should be set
            assertNotNull(seenClip.get());
            assertEquals(clipShape.getBounds2D(), seenClip.get().getBounds2D());

            // Parent unchanged
            assertEquals(originalTransform, parent.getTransform());
            assertEquals(originalClip, parent.getClip());
        } finally {
            parent.dispose();
        }
    }

    @Test
    void render_usesChildGraphics_soMutationsDontAffectParent() {
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D parent = img.createGraphics();
        try {
            parent.setColor(Color.BLUE); // known parent color

            AtomicReference<Graphics2D> childRef = new AtomicReference<>();

            Consumer<Graphics2D> renderable = g -> {
                childRef.set(g);
                g.setColor(Color.RED); // mutate child only
            };

            GraphicsTransformBehaviour behaviour =
                    GraphicsTransformBehaviour.buildBehaviour(renderable, null, null);

            behaviour.transformAndRender(parent);

            // renderable should receive a different Graphics2D instance (the child)
            assertNotNull(childRef.get());
            assertNotSame(parent, childRef.get());

            // Parent color should remain BLUE, not RED
            assertEquals(Color.BLUE, parent.getColor());
        } finally {
            parent.dispose();
        }
    }

}