package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.camera.PanZoomBehaviour;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.componentManagement.UIComponentManager;

public class PanZoomPaneSkeleton{

    private HitboxRect boundary;
    private PanZoomBehaviour behaviour;
    private UIComponentManager componentManager;


    protected PanZoomPaneSkeleton(){}


    public HitboxRect getBoundary(){
        return boundary;
    }

    void setBoundary(HitboxRect boundary){
        this.boundary = boundary;
    }

    public PanZoomBehaviour getBehaviour(){
        return behaviour;
    }

    void setBehaviour(PanZoomBehaviour behaviour){
        this.behaviour = behaviour;
    }

    public UIComponentManager getComponentManager(){
        return componentManager;
    }

    void setComponentManager(UIComponentManager componentManager){
        this.componentManager = componentManager;
    }

}
