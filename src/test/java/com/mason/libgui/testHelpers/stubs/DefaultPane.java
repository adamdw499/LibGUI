package com.mason.libgui.testHelpers.stubs;

import com.mason.libgui.components.panes.PanZoomPane;
import com.mason.libgui.components.panes.PanZoomPaneSkeleton;
import com.mason.libgui.utils.structures.Size;

import java.awt.*;

public class DefaultPane extends PanZoomPane{


    private static final int BORDER_WIDTH = 5;
    private final Size size;


    public DefaultPane(PanZoomPaneSkeleton skeleton){
        super(skeleton);
        this.size = skeleton.getBoundary().getSize();
    }


    @Override
    protected void renderAfterTranslation(Graphics2D g){
        super.renderAfterTranslation(g);
        drawBorder(g);
    }

    protected void drawBorder(Graphics2D g){
        g.setColor(new Color(100, 100, 130));
        g.fillRect(0, 0, size.width(), BORDER_WIDTH);
        g.fillRect(0, 0, BORDER_WIDTH, size.height());
        g.fillRect(0, size.height() - BORDER_WIDTH, size.width(), BORDER_WIDTH);
        g.fillRect(size.width() - BORDER_WIDTH, 0, BORDER_WIDTH, size.height());
    }

}
