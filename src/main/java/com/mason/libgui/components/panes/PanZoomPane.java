package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.camera.PanZoomBehaviour;

import java.awt.*;

public class PanZoomPane extends Pane{


    private final PanZoomBehaviour panZoomBehaviour;


    protected PanZoomPane(PanZoomPaneSkeleton skeleton){
        super(skeleton.getBoundary(), skeleton.getComponentManager());
        this.panZoomBehaviour = skeleton.getBehaviour();
        panZoomBehaviour.setInputSource(skeleton.getComponentManager().getInputDistributor());
    }


    protected void renderRawPane(Graphics2D g){
        super.renderAfterTranslation(g);
    }

    @Override
    protected void renderAfterTranslation(Graphics2D g){
        panZoomBehaviour.renderAfterTranslation(g);
    }

}
