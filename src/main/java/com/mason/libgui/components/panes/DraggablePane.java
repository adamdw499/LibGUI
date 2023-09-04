
package com.mason.libgui.components.panes;

import com.mason.libgui.components.dragging.Draggable;
import com.mason.libgui.utils.StyleInfo;

/**
 * A Pane object that can be dragged around.
 * @author Adam Whittaker
 */
public class DraggablePane extends Pane implements Draggable{


    /**
     * Forwarded constructor.
     */
    public DraggablePane(StyleInfo info, int x, int y, int w, int h){
        super(info, x, y, w, h);
    }


    /**
     * Checks if the mouse is on the border.
     * @param mx relative mouse x
     * @param my relative mouse y
     */
    @Override
    public boolean withinDragRegion(int mx, int my){
        return mx<x+info.getLineWidth() || x+width-info.getLineWidth()<mx ||
                (my<y+info.getLineWidth()) || y+height-info.getLineWidth()<my;
    }

    @Override
    public boolean validDragLocation(int x, int y){
        return true;
    }

    @Override
    public void processInvalidDrag(int mx, int my){}

    @Override
    public void startDrag(){}

    @Override
    public void releaseDrag(){}

}
