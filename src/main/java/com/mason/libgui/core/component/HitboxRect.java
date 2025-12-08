package com.mason.libgui.core.component;

import com.mason.libgui.utils.structures.*;

public class HitboxRect implements Hitbox, RectQuery{


    private Coord topLeft;
    private Size size;


    public HitboxRect(Coord topLeft, Size size){
        this.topLeft = topLeft;
        this.size = size;
    }


    @Override
    public boolean withinBounds(Coord c){
        return toRect().withinBounds(c);
    }

    @Override
    public void setCoord(Coord c){
        topLeft = c;
    }

    @Override
    public Coord getCoord(){
        return topLeft;
    }

    @Override
    public Size getSize(){
        return size;
    }

    public void setSize(Size size){
        this.size = size;
    }

    public Rect toRect(){
        return Rect.buildRect(topLeft, size);
    }


    public void clampWithinBoundary(RectQuery boundary){
        int newX = getClampedX(boundary);
        int newY = getClampedY(boundary);
        setCoord(new Coord(newX, newY));
    }

    private int getClampedX(RectQuery boundary){
        int x = topLeft.x();
        int boundaryX = boundary.getCoord().x();
        int boundaryWidth = boundary.getSize().width();
        if(x < boundaryX){
            x = boundaryX;
        }else if(x > boundaryX + boundaryWidth){
            x = boundaryX + boundaryWidth - size.width();
        }
        return x;
    }

    private int getClampedY(RectQuery boundary){
        int y = topLeft.y();
        int boundaryY = boundary.getCoord().y();
        int boundaryHeight = boundary.getSize().height();
        if(y < boundaryY){
            y = boundaryY;
        }else if(y > boundaryY + boundaryHeight){
            y = boundaryY + boundaryHeight - size.height();
        }
        return y;
    }


}
