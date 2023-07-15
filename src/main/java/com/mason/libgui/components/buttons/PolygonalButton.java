package com.mason.libgui.components.buttons;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

/**
 * A button in the shape of a given polygon.
 */
public abstract class PolygonalButton extends Button{


    /**
     * The shape of the button.
     */
    private Polygon polygon;


    /**
     * Creates an instance.
     * @param info
     * @param poly
     */
    public PolygonalButton(StyleInfo info, Polygon poly){
        super(info, poly.getBounds().x, poly.getBounds().y, poly.getBounds().width, poly.getBounds().height);
        this.polygon = poly;
    }


    @Override
    public void render(Graphics2D g){
        if(isHovering()) g.setColor(info.FORE_HIGHLIGHT);
        else g.setColor(info.FOREGROUND);
        g.fillPolygon(polygon);
        info.RENDER_UTILS.drawBorder(g, info, polygon);
    }


    @Override
    public boolean withinBounds(int mx, int my){
        return polygon.contains(mx, my);
    }

    @Override
    public void setX(int _x){
        polygon.translate(_x - x, 0);
        super.setX(_x);
    }

    @Override
    public void setY(int _y){
        polygon.translate(0, _y - y);
        super.setY(_y);
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

}
