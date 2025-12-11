package com.mason.libgui.components.panes;

import java.awt.*;

public abstract class RenderReadyPanZoomPane extends PanZoomPane{


    public RenderReadyPanZoomPane(PanZoomPaneSkeleton skeleton){
        super(skeleton);
    }


    @Override
    protected void renderAfterTranslation(Graphics2D g){
        drawBackground(g);
        super.renderAfterTranslation(g);
        drawForeground(g);
    }

    protected abstract void drawBackground(Graphics2D g);

    protected abstract void drawForeground(Graphics2D g);

}
