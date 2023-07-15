package com.mason.libgui.components.buttons;

import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A button in the shape of a trapezoid
 */
public abstract class TrapezoidalButton extends PolygonalButton{


    /**
     * Tapering is the pixel difference in length between the main parallel sides.
     * Direction is the way in which the smaller side of the main parallel side points. I.e: the direction the "arrow"
     * of the trapezium is pointing.
     */
    private final int tapering;
    private final Direction direction;


    public TrapezoidalButton(StyleInfo info, int x, int y, int w, int h, int tapering, Direction direction){
        super(info, createTrapezium(x, y, w, h, tapering, direction));
        this.tapering = tapering;
        this.direction = direction;
    }


    private static Polygon createTrapezium(int x, int y, int width, int height, int tapering, Direction direction){
        switch(direction){
            case LEFT -> {
                return new Polygon(
                        new int[]{x+width, x, x, x+width},
                        new int[]{y, y+tapering, y-tapering+height, y+height}, 4);
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
                        new int[]{y, y+tapering, y-tapering+height, y+height}, 4);
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
    }


    @Override
    public void setWidth(int w){
        super.setWidth(w);
        setPolygon(createTrapezium(x, y, width, height, tapering, direction));
    }

    @Override
    public void setHeight(int h){
        super.setHeight(h);
        setPolygon(createTrapezium(x, y, width, height, tapering, direction));
    }


    public static TrapezoidalButton getBlankButton(StyleInfo info, int x, int y, int w, int h, int tapering, Direction direction){
        return new TrapezoidalButton(info, x, y, w, h, tapering, direction){
            @Override
            public void mouseClicked(MouseEvent e){}
        };
    }


}
