package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.components.behaviour.GraphicsTransformBehaviour;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Coord;
import com.mason.libgui.utils.structures.RectQuery;
import com.mason.libgui.utils.structures.Size;
import com.mason.libgui.utils.structures.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

public class Viewport{


    private final RectQuery boundingRect;
    private final HitboxRect view;
    private final Zoom zoom;
    private final GraphicsTransformBehaviour graphicsTransformer;


    private Viewport(Consumer<Graphics2D> renderable, RectQuery boundingRect, RectQuery initialView, Zoom zoom){
        this.boundingRect = boundingRect;
        this.view = new HitboxRect(initialView.getCoord(), initialView.getSize());
        this.zoom = zoom;
        graphicsTransformer = GraphicsTransformBehaviour.buildTransformOnlyBehaviour(renderable, this::constructTransform);
    }

    private AffineTransform constructTransform(){
        Coord viewCoord = view.getCoord();
        double zoom = getZoom();

        AffineTransform transform = AffineTransform.getScaleInstance(zoom, zoom);
        transform.translate(-viewCoord.x(), -viewCoord.y());
        return transform;
    }

    static Viewport buildViewport(Consumer<Graphics2D> renderable, RectQuery boundingRect, RectQuery initialView, Zoom zoom){
        return new Viewport(renderable, boundingRect, initialView, zoom);
    }

    static Viewport buildViewportWithDefaultZoomAndInitialView(Consumer<Graphics2D> renderable, RectQuery boundingRect){
        Rect initialView = Rect.buildRect(new Coord(0, 0), boundingRect.getSize());
        return new Viewport(renderable, boundingRect, initialView, Zoom.buildDefaultFullyZoomedOutZoom());
    }


    Coord apparentToScreen(Coord apparent){
        double zoom = getZoom();
        Coord topLeft = view.getCoord();
        int x = (int)((apparent.x() - topLeft.x()) * zoom);
        int y = (int)((apparent.y() - topLeft.y()) * zoom);
        return new Coord(x, y);
    }

    Coord screenToApparent(Coord screen){
        double zoom = getZoom();
        Coord topLeft = view.getCoord();
        int x = (int)(screen.x()/zoom + topLeft.x());
        int y = (int)(screen.y()/zoom + topLeft.y());
        return new Coord(x, y);
    }

    double getZoom(){
        return zoom.getZoom();
    }


    void mouseWheel(MouseInputEvent event){
        zoom.mouseWheel(event);
    }

    void resizeAfterZoom(double zoomFactorChange){
        Size size = view.getSize();
        view.setSize(new Size((int)(size.width()/zoomFactorChange), (int)(size.height()/zoomFactorChange)));
    }

    void clampWithinBoundary(){
        view.clampWithinBoundary(boundingRect);
    }


    void setTopLeft(Coord c){
        view.setCoord(c);
    }

    Coord getTopLeft(){
        return view.getCoord();
    }


    void renderAfterTranslation(Graphics2D g){
        graphicsTransformer.transformAndRender(g);
    }

}
