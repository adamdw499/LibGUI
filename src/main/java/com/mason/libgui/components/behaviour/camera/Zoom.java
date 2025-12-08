package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.core.input.mouse.MouseInputEvent;

public class Zoom{


    private final double minZoom;
    private final double maxZoom;
    private final double zoomFactor;
    private double zoom;


    private Zoom(double minZoom, double maxZoom, double zoomFactor, double initialZoom){
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.zoomFactor = zoomFactor;
        this.zoom = initialZoom;
    }

    static Zoom buildZoom(double minZoom, double maxZoom, int numZoomLevels, double initialZoom){
        if(numZoomLevels <= 1) {
            throw new IllegalArgumentException("numZoomLevels must be >= 2");
        }
        double zoomFactor = calculateZoomFactor(maxZoom/minZoom, numZoomLevels);
        return new Zoom(minZoom, maxZoom, zoomFactor, initialZoom);
    }

    private static double calculateZoomFactor(double zoomRange, int numZoomLevels){
        return Math.pow(zoomRange, 1D/(numZoomLevels-1));
    }

    static Zoom buildZoomWithDefaultNumZoomLevels(double minZoom, double maxZoom){
        return buildZoom(minZoom, maxZoom, 9, 1);
    }

    static Zoom buildDefaultMiddleZoom(){
        return buildZoomWithDefaultNumZoomLevels(1D/16, 16);
    }

    static Zoom buildDefaultFullyZoomedOutZoom(){
        return buildZoomWithDefaultNumZoomLevels(1, 16*16);
    }


    double getZoom(){
        return zoom;
    }


    void mouseWheel(MouseInputEvent event){
        int wheelTicks = event.getWheelMotion();
        double totalZoomFactor = Math.pow(zoomFactor, -wheelTicks);
        zoom *= totalZoomFactor;
        clampZoom();
    }

    private void clampZoom(){
        if(zoom < minZoom){
            zoom = minZoom;
        }else if(zoom > maxZoom){
            zoom = maxZoom;
        }
    }

}
