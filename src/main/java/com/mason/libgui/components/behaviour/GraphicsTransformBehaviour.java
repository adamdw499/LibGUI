package com.mason.libgui.components.behaviour;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GraphicsTransformBehaviour{


    private final Consumer<Graphics2D> renderable;
    private final Supplier<AffineTransform> transformSupplier;
    private final Supplier<Shape> clipSupplier;


    private GraphicsTransformBehaviour(Consumer<Graphics2D> renderable, Supplier<AffineTransform> transformSupplier, Supplier<Shape> clipSupplier){
        this.renderable = renderable;
        this.transformSupplier = transformSupplier;
        this.clipSupplier = clipSupplier;
    }

    public static GraphicsTransformBehaviour buildBehaviour(Consumer<Graphics2D> renderable, Supplier<AffineTransform> transformSupplier, Supplier<Shape> clipSupplier){
        return new GraphicsTransformBehaviour(renderable, transformSupplier, clipSupplier);
    }

    public static GraphicsTransformBehaviour buildTransformOnlyBehaviour(Consumer<Graphics2D> renderable, Supplier<AffineTransform> transformSupplier){
        return new GraphicsTransformBehaviour(renderable, transformSupplier, null);
    }

    public static GraphicsTransformBehaviour buildClipOnlyBehaviour(Consumer<Graphics2D> renderable, Supplier<Shape> clipSupplier){
        return new GraphicsTransformBehaviour(renderable, null, clipSupplier);
    }


    public void transformAndRender(Graphics2D g){
        Graphics2D child = (Graphics2D) g.create();
        try{
            renderWithTemporaryGraphics(child);
        }finally{
            child.dispose();
        }
    }

    private void renderWithTemporaryGraphics(Graphics2D g){
        transformGraphics(g);
        clipGraphics(g);
        renderable.accept(g);
    }

    private void transformGraphics(Graphics2D g){
        if(transformSupplier != null){
            AffineTransform transform = transformSupplier.get();
            g.transform(transform);
        }
    }

    private void clipGraphics(Graphics2D g){
        if(clipSupplier != null){
            Shape clip = clipSupplier.get();
            g.clip(clip);
        }
    }

}
