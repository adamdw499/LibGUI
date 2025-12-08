
package com.mason.libgui.core.component;

import com.mason.libgui.utils.structures.Coord;

import java.awt.*;

public abstract class UIComponent implements Hitbox{
    

    private final Hitbox hitbox;
    
    
    public UIComponent(Hitbox hitbox){
        this.hitbox = hitbox;
    }


    @Override
    public boolean withinBounds(Coord c){
        return hitbox.withinBounds(c);
    }

    @Override
    public void setCoord(Coord c){
        hitbox.setCoord(c);
    }

    @Override
    public Coord getCoord(){
        return hitbox.getCoord();
    }


    public abstract void render(Graphics2D g);
    
    public abstract void tick();
    
}
