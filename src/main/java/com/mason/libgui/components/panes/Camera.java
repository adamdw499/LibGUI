package com.mason.libgui.components.panes;

import com.mason.libgui.components.dragging.DraggableComponent;
import com.mason.libgui.core.UIComponentManager;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

import static java.lang.Math.min;
import static java.lang.StrictMath.pow;

/**
 * A camera for panning and zooming over a pane.
 */
public class Camera extends DraggableComponent{


    /**
     * zoomEnabled: Whether zooming is allowed
     * panning: whether the camera is currently panning
     * parentWidth, parentHeight: dimensions of the parent component
     * zoom: the factor by which the camera is zoomed
     * zoomFactor: the factor by which the zoom level changes upon moving the mouse wheel
     * maxZoom, minZoom: The bounds for the level of zoom
     */
    private boolean zoomEnabled;
    private boolean panning;
    private int parentWidth, parentHeight;
    private double zoom = 1.0;
    private double zoomFactor;
    private double maxZoom;
    private double minZoom;


    /**
     * Sets the bounds and whether the zoom is enabled.
     */
    public Camera(int x, int y, int w, int h, boolean zoomEnabled){
        super(x, y, w, h);
        this.zoomEnabled = zoomEnabled;
    }


    /**
     * Calculates bounds relative to apparent coordinates.
     * @param mx The x coordinate.
     * @param my The y coordinate.
     */
    @Override
    public boolean withinBounds(int mx, int my){
        return withinBounds((int)apparentX(), (int)apparentY(), (int)(width/zoom), (int)(height/zoom), mx, my);
    }

    /**
     * Handles the zoom
     * @param me the event to be processed
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent me){
        if(zoomEnabled && !panning){
            double absX = absX(me.getX()),
                absY = absY(me.getY());
            if(me.getWheelRotation() < 0){
                if(zoom < maxZoom){
                    zoom *= zoomFactor;
                }
            }else{
                if(zoom > minZoom){
                    zoom /= zoomFactor;
                }
            }
            setApparentX(me.getX() - absX/zoom);
            setApparentY(me.getY() - absY/zoom);
        }
    }

    /**
     * Camera is always in the background.
     * @param x relative mouse x
     * @param y relative mouse y
     */
    @Override
    public boolean withinDragRegion(int x, int y){
        return true;
    }

    /**
     * Calculates drag location relative to apparent coordinates.
     * @param x relative coordinates
     * @param y relative coordinates
     */
    @Override
    public boolean validDragLocation(int x, int y){
        double ax = apparentX(x);
        double ay = apparentY(y);
        return ax>=0 && ay>=0 && ax+width/zoom<=parentWidth && ay+height/zoom<=parentHeight;
    }

    /**
     * Keeps the camera in bounds
     * @param mx parent coordinates
     * @param my parent coordinates
     */
    @Override
    public void processInvalidDrag(int mx, int my){
        if(mx < 0) setApparentX(0);
        else if(mx+width/zoom > parentWidth) setApparentX(parentWidth - width/zoom);
        else setApparentX(mx);
        if(my < 0) setApparentY(0);
        else if(my+height/zoom > parentHeight) setApparentY(parentHeight - height/zoom);
        else setApparentY(my);
    }

    /**
     * Tells the camera it is panning
     */
    @Override
    public void startDrag(){
        panning = true;
    }

    /**
     * Tells the camera it is no longer dragging
     */
    @Override
    public void releaseDrag(){
        panning = false;
    }


    /**
     * Moves the camera so that the given coords are in the center
     * @param mx relative coords
     * @param my relative coords
     */
    public void focus(int mx, int my){
        setX(mx - width/2D);
        setY(my - height/2D);
    }

    /**
     * @return the relative x of the camera.
     */
    public int getFocusX(){
        return (int)(x + width/2D);
    }

    /**
     * @return the relative y of the camera.
     */
    public int getFocusY(){
        return (int)(y + height/2D);
    }

    /**
     * @return apparent top left corner of the camera
     */
    public double apparentX(){
        return apparentX(x);
    }

    /**
     * @return apparent top left corner of the camera
     */
    public double apparentY(){
        return apparentY(y);
    }

    /**
     * translates the given top left camera coord to an apparent coord
     * @param x relative coord
     */
    private double apparentX(double x){
        return x + (width/2D) - width/(2D*zoom);
    }

    /**
     * translates the given top left camera coord to an apparent coord
     * @param y relative coord
     */
    private double apparentY(double y){
        return y + height/2D - height/(2D*zoom);
    }

    /**
     * Sets the apparent coord
     * @param ax apparent coord
     */
    public void setApparentX(double ax){
        setX(ax - width/2D + width/(2D*zoom));
    }

    /**
     * Sets the apparent coord
     * @param ay apparent coord
     */
    public void setApparentY(double ay){
        setY(ay - height/2D + height/(2D*zoom));
    }


    public void setMinZoom(double minZoom){
        this.minZoom = minZoom;
    }

    public void setMaxZoom(double maxZoom){
        this.maxZoom = maxZoom;
    }

    public void setZoomFactor(double zoomFactor){
        this.zoomFactor = zoomFactor;
    }

    public double getZoom(){
        return zoom;
    }

    @Override
    public void render(Graphics2D g){}

    @Override
    public void tick(int mx, int my){}

    /**
     * Records the dimensions of the parent.
     * @param parent the parent
     */
    @Override
    public void setParent(UIComponentManager parent){
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();
        if(width < parentWidth && height < parentHeight){
            maxZoom = min(parentWidth/width, parentHeight/height);
            zoomFactor = pow(maxZoom, 0.25);
            minZoom = maxZoom/pow(zoomFactor, 8);
        }else{
            maxZoom = 1;
            zoomFactor = 1.2;
            minZoom = 1/pow(zoomFactor,8);
        }
    }

    /**
     * Calculates the absolute coordinate, i.e: the distance of the point from the top left of the camera as seen on
     * the parent.
     * @param mx the coord
     */
    public double absX(int mx){
        return (mx - apparentX()) * zoom;
    }

    /**
     * Calculates the absolute coordinate, i.e: the distance of the point from the top left of the camera as seen on
     * the parent.
     * @param my the coord
     */
    public double absY(int my){
        return (my - apparentY()) * zoom;
    }

    /**
     * convenience casting method
     * @param x relative coord as a double
     */
    public void setX(double x){
        setX((int)x);
    }

    /**
     * convenience casting method
     * @param y relative coord as a double
     */
    public void setY(double y){
        setY((int)y);
    }

    public boolean isZoomEnabled(){
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled){
        this.zoomEnabled = zoomEnabled;
    }

}

