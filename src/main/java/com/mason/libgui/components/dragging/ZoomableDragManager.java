package com.mason.libgui.components.dragging;

import com.mason.libgui.components.panes.Camera;

import java.awt.event.MouseEvent;

/**
 * A drag manager that can handle being zoomed.
 */
public class ZoomableDragManager extends DragManager{


    /**
     * camera: camera
     * draggingCamera: whether the camera is being dragged.
     * invX, invY: Storage variables for camera dragging.
     */
    private final Camera camera;
    private boolean draggingCamera = false;
    private double invX, invY;


    public ZoomableDragManager(Camera camera){
        this.camera = camera;
    }


    /**
     * Moves the component, taking into account the level of zoom.
     */
    @Override
    public void mouseDragged(MouseEvent e){
        if(draggingCamera){
            int nx = (int)(invX - camera.absX(e.getX())/camera.getZoom());
            int ny = (int)(invY - camera.absY(e.getY())/camera.getZoom());
            if(component.validDragLocation(nx, ny)){
                component.setX(nx);
                component.setY(ny);
                component.mouseDragged(e);
            }else{
                component.processInvalidDrag(nx, ny);
            }
        }else{
            super.mouseDragged(e);
        }
    }

    /**
     * Calculates relative coordinates, initialises drag and notifies the component that it is being dragged, taking
     * into account the level of zoom.
     * @param comp The thing being dragged
     * @param mx absolute x coordinate
     * @param my absolute y coordinate
     */
    @Override
    public void startDrag(Draggable comp, int mx, int my){
        super.startDrag(comp, mx, my);
        if(component instanceof Camera){
            draggingCamera = true;
            invX = camera.absX(mx)/camera.getZoom() + component.getX();
            invY = camera.absY(my)/camera.getZoom() + component.getY();
        }
    }

    /**
     * Shuts off drag and notifies the component that it is being released.
     */
    public void releaseDrag(){
        super.releaseDrag();
        if(draggingCamera) draggingCamera = false;
    }

    /**
     * @return the camera.
     */
    public Camera getCamera(){
        return camera;
    }

}
