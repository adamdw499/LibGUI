
package com.mason.libgui.components.panes;

import com.mason.libgui.components.dragging.DragManager;
import com.mason.libgui.core.UIComponentManager;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

/**
 * A window pane onto which new components can be nested. Mainly code for translating between absolute and relative
 * mouse coordinates.
 * @author Adam Whittaker
 */
public class Pane extends UIComponentManager{


    /**
     * Forwarded constructor
     */
    public Pane(StyleInfo info, int x, int y, int w, int h){
        super(info, x, y, w, h);
    }

    /**
     * Forwarded constructor
     */
    protected Pane(DragManager m, StyleInfo info, int x, int y, int w, int h){
        super(m, info, x, y, w, h);
    }


    /**
     * Translates the components, and renders a border around the pane.
     * @param g the graphics object
     */
    @Override
    public void render(Graphics2D g){
        renderComponents(g);
        renderBorder(g);
    }

    /**
     * Applies a translation to the components before drawing them, to translate from absolute to relative
     * coordinates.
     */
    protected void renderComponents(Graphics2D g){
        AffineTransform saved = g.getTransform();
        g.transform(AffineTransform.getTranslateInstance(x, y));
        super.render(g);
        g.setTransform(saved);
    }

    /**
     * Draws a border around the pane.
     */
    protected void renderBorder(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, info, x, y, width, height);
    }

    /**
     * Translates the mouse event to relative coordinates.
     */
    protected MouseEvent relativeMouseCoords(MouseEvent e){
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                e.getX() - x, e.getY() - y, e.getClickCount(), e.isPopupTrigger());
    }

    /**
     * Translates the mouse wheel event to relative coordinates.
     */
    protected MouseWheelEvent relativeMouseCoords(MouseWheelEvent e){
        return new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - x, 
                e.getY() - y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), 
                e.getWheelRotation());
    }

    
    @Override
    public void mouseClicked(MouseEvent e){
        e = relativeMouseCoords(e);
        super.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e){
        e = relativeMouseCoords(e);
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e){
        e = relativeMouseCoords(e);
        super.mouseReleased(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        e = relativeMouseCoords(e);
        super.mouseWheelMoved(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        e = relativeMouseCoords(e);
        super.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        e = relativeMouseCoords(e);
        super.mouseMoved(e);
    }
    
}
