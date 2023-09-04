package com.mason.libgui.components.buttons;

import com.mason.libgui.components.misc.Tooltip;
import com.mason.libgui.core.UIComponentManager;
import com.mason.libgui.utils.StyleInfo;

import java.awt.event.MouseEvent;

/**
 * A button with a tooltip that appears when you hover over it.
 */
public abstract class TooltipButton extends Button{


    private Tooltip tooltip;
    private UIComponentManager parent;


    /**
     * Creates an instance.
     * @param parent The pane the button will be placed in.
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     * @param tooltipText
     * @param tooltipStyle The style info for the tooltip.
     */
    public TooltipButton(UIComponentManager parent, StyleInfo info, int x, int y, int w, int h, String tooltipText, StyleInfo tooltipStyle){
        super(info, x, y, w, h);
        this.parent = parent;
        tooltip = new Tooltip(tooltipText, tooltipStyle, x, -1, Math.min((int)(width*2.5), parent.getWidth()-x));
        tooltip.setToMinWidth();
        tooltip.setY(y - tooltip.getHeight());
    }

    /**
     * Creates an instance, with default style info for the button, and alpha style info for the tooltip.
     * @param parent
     * @param x
     * @param y
     * @param w
     * @param h
     * @param tooltipText
     */
    public TooltipButton(UIComponentManager parent, int x, int y, int w, int h, String tooltipText){
        this(parent, StyleInfo.DEFAULT_STYLE_INFO, x, y, w, h, tooltipText, StyleInfo.ALPHA_STYLE_INFO);
    }

    /**
     * Creates an instance without specifying the parent component manager.
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     * @param tooltipText
     * @param tooltipStyle
     */
    public TooltipButton(StyleInfo info, int x, int y, int w, int h, String tooltipText, StyleInfo tooltipStyle){
        super(info, x, y, w, h);
        tooltip = new Tooltip(tooltipText, tooltipStyle, x, -1, (int)(width*2.5));
        tooltip.setToMinWidth();
        tooltip.setY(y - tooltip.getHeight());
    }


    /**
     * Adds the tooltip to the component manager
     */
    @Override
    protected void startHovering(){
        super.startHovering();
        parent.addComponent(tooltip);
    }

    /**
     * Removes the tooltip from the component manager.
     */
    @Override
    protected void stopHovering(){
        super.stopHovering();
        parent.removeComponent(tooltip);
    }

    @Override
    public void setX(int _x){
        super.setX(_x);
        if(x + tooltip.getWidth() <= parent.getWidth()){
            tooltip.setX(_x);
        }else{
            tooltip.setX(parent.getWidth() - tooltip.getWidth());
        }
    }

    @Override
    public void setY(int _y){
        super.setY(_y);
        if(y >= tooltip.getHeight()){
            tooltip.setY(_y - tooltip.getHeight());
        }else{
            tooltip.setY(_y + height);
        }
    }

    @Override
    public void setParent(UIComponentManager p){
        if(parent == null){
            parent = p;
            tooltip.setWidth(Math.min((int)(width*2.5), parent.getWidth()-x));
            tooltip.setToMinWidth();
        }
    }

    public void setTooltip(Tooltip tooltip){
        this.tooltip = tooltip;
    }


    public static TooltipButton getBlankButton(UIComponentManager parent, int x, int y, int w, int h, String tooltipText){
        return new TooltipButton(parent, x, y, w, h, tooltipText){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

    public static TooltipButton getBlankButton(int x, int y, int w, int h, String tooltipText){
        return new TooltipButton(StyleInfo.DEFAULT_STYLE_INFO, x, y, w, h, tooltipText, StyleInfo.ALPHA_STYLE_INFO){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

}
