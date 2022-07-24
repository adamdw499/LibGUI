package com.mason.libgui.components.buttons;

import com.mason.libgui.utils.RenderUtils;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class TrapezoidalButton extends Button{


    private int tapering;
    private Polygon trapezium;
    private Direction direction;


    public TrapezoidalButton(StyleInfo info, int x, int y, int w, int h, int tapering, Direction direction){
        super(info, x, y, w, h);
        this.tapering = tapering;
        this.direction = direction;
        trapezium = getPolygon();
    }


    private Polygon getPolygon(){
        switch(direction){
            case LEFT -> {
                return new Polygon(
                        new int[]{x+width, x, x, x+width},
                        new int[]{y, y+tapering, y+tapering+height, y+height}, 4);
            }
            case UP -> {
                return new Polygon(
                        new int[]{x, x+tapering, x+width-tapering, x+width},
                        new int[]{y+height, y, y, y+height}, 4);
            }
            case DOWN -> {
                return new Polygon(
                    new int[]{x, x+tapering, x+width-tapering, x+width},
                    new int[]{y, y+height, y+height, y}, 4);
            }
            case RIGHT -> {
                return new Polygon(
                        new int[]{x, x+width, x+width, x},
                        new int[]{y, y+tapering, y+tapering+height, y+height}, 4);
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
    }


    @Override
    public void render(Graphics2D g){
        if(isHovering()) g.setColor(style.FORE_HIGHLIGHT);
        else g.setColor(style.FOREGROUND);
        g.fillPolygon(trapezium);
        RenderUtils.drawBorder(g, style, trapezium);
    }


    @Override
    public boolean withinBounds(int mx, int my){
        return trapezium.contains(mx, my);
    }

    @Override
    public void setX(int _x){
        trapezium.translate(_x - x, 0);
        super.setX(_x);
    }

    @Override
    public void setY(int _y){
        trapezium.translate(0, _y - y);
        super.setY(_y);
    }

    @Override
    public void setWidth(int w){
        super.setWidth(w);
        trapezium = getPolygon();
    }

    @Override
    public void setHeight(int h){
        super.setHeight(h);
        trapezium = getPolygon();
    }


    public static TrapezoidalButton getBlankButton(StyleInfo info, int x, int y, int w, int h, int tapering, Direction direction){
        return new TrapezoidalButton(info, x, y, w, h, tapering, direction){
            @Override
            public void mouseClicked(MouseEvent e){}
        };
    }


}
