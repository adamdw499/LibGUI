package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.GraphicsTransformBehaviour;
import com.mason.libgui.utils.structures.Coord;
import com.mason.libgui.utils.structures.RectQuery;
import com.mason.libgui.utils.structures.Size;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

public class PaneGraphicsTransformBuilder{


    private final Consumer<Graphics2D> renderable;
    private final RectQuery boundary;


    public PaneGraphicsTransformBuilder(Consumer<Graphics2D> renderable, RectQuery boundary){
        this.renderable = renderable;
        this.boundary = boundary;
    }


    public GraphicsTransformBehaviour build(){
        return GraphicsTransformBehaviour.buildBehaviour(renderable, this::supplyTransform, this::supplyClip);
    }

    private AffineTransform supplyTransform(){
        Coord topLeft = boundary.getCoord();
        return AffineTransform.getTranslateInstance(topLeft.x(), topLeft.y());
    }

    private Shape supplyClip(){
        Size size = boundary.getSize();
        return new Rectangle(0, 0, size.width(), size.height());
    }

}
