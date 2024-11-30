package com.mason.libgui.components.panes;

import com.mason.libgui.components.dragging.*;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.test.testComponents.PaneBorderTest;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import static com.mason.libgui.utils.Utils.withinRectBounds;

/**
 * A pane that can be panned and zoomed.
 */
public class PannablePane extends Pane{


    /**
     * The camera to view the pane. Its dimensions are seen in the parent.
     */
    private final Camera camera;


    /**
     * Creates an instance
     * @param info style info
     * @param x the x of the viewport in the parent
     * @param y the y of the view port in the parent
     * @param w the width of the pane
     * @param h the height of the pane
     * @param vx where the camera is within the pane
     * @param vy where the camera is within the pane
     * @param vw the width of the camera
     * @param vh the height of the camera
     * @param zoomEnabled whether zooming is enabled
     */
    public PannablePane(StyleInfo info, int x, int y, int w, int h, int vx, int vy, int vw, int vh, boolean zoomEnabled){
        super(new ZoomableDragManager(new Camera(vx, vy, vw, vh, zoomEnabled)), info, x, y, w, h);
        camera = ((ZoomableDragManager) dragManager).getCamera();
        addComponent(camera);
        addComponent(new PaneBorderTest(0, 0, w, h));
    }

    /**
     * Creates an instance with the camera in the center
     * @param info style info
     * @param x the x of the viewport in the parent
     * @param y the y of the view port in the parent
     * @param w the width of the pane
     * @param h the height of the pane
     * @param vw the width of the camera
     * @param vh the height of the camera
     * @param zoomEnabled whether zooming is enabled
     */
    public PannablePane(StyleInfo info, int x, int y, int w, int h, int vw, int vh, boolean zoomEnabled){
        this(info, x, y, w, h, (w-vw)/2, (h-vh)/2, vw, vh, zoomEnabled);
    }


    /**
     * The dimensions relative to the parent are those of the camera.
     * @param mx The x coordinate.
     * @param my The y coordinate.
     */
    @Override
    public boolean withinBounds(int mx, int my){
        return withinRectBounds(x, y, camera.getWidth(), camera.getHeight(), mx, my);
    }

    /**
     * Tests whether the component is seen by the camera.
     */
    @Utils.Unfinished
    protected boolean inView(UIComponent comp){
        return camera.intersects(comp);
    }


    /**
     * Draws the border of the viewport with the dimensions of the camera.
     */
    @Override
    protected void renderBorder(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, StyleInfo.DEFAULT_STYLE_INFO, x, y, camera.getWidth(), camera.getHeight());
    }

    /**
     * Renders components that can be seen, and under the given zoom level.
     */
    @Override
    protected void renderComponents(Graphics2D g){
        double zoom = camera.getZoom();
        g.setClip(x, y, camera.getWidth(), camera.getHeight());
        AffineTransform saved = g.getTransform();
        g.transform(AffineTransform.getScaleInstance(zoom, zoom));
        g.transform(AffineTransform.getTranslateInstance(
                x/zoom - camera.apparentX(),
                y/zoom - camera.apparentY()
        ));
        for(int n = componentNum()-1; n>=0; n--){
            /*if(inView(components.get(n))) */getComponent(n).render(g);
        }
        g.setTransform(saved);
        g.setClip(null);
    }


    /**
     * Changes the mouse coords to the relative coords on the pane.
     */
    @Override
    protected MouseEvent relativeMouseCoords(MouseEvent e){
        double zoom = camera.getZoom();
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                (int)((e.getX() - x)/zoom + camera.apparentX()),
                (int)((e.getY() - y)/zoom + camera.apparentY()),
                e.getClickCount(), e.isPopupTrigger());
    }

    /**
     * Changes the mouse coords to the relative coords on the pane.
     */
    @Override
    protected MouseWheelEvent relativeMouseCoords(MouseWheelEvent e){
        double zoom = camera.getZoom();
        return new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                (int)((e.getX() - x)/zoom + camera.apparentX()),
                (int)((e.getY() - y)/zoom + camera.apparentY()),
                e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation());
    }

}
