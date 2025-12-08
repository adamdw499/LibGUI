package com.mason.libgui.components.behaviour.drag;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.input.mouse.MouseInputBounder;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Boundable;
import com.mason.libgui.utils.structures.Coord;

public class StandardDragBehaviour extends AbstractDragBehaviour{


    private final Hitbox dragRegion;
    private Coord initialDragOffset;


    public StandardDragBehaviour(Hitbox dragRegion){
        super();
        this.dragRegion = dragRegion;
    }


    @Override
    public boolean withinBounds(Coord c){
        return dragRegion.withinBounds(c);
    }

    Hitbox getDragRegion(){
        return dragRegion;
    }


    @Override
    protected void onDragStart(MouseInputEvent event){
        initialDragOffset = calculateInitialOffset(event.getCoord());
    }

    private Coord calculateInitialOffset(Coord mouse){
        return new Coord(mouse.x() - dragRegion.getCoord().x(), mouse.y() - dragRegion.getCoord().y());
    }


    @Override
    protected void onDragIncrement(MouseInputEvent event){
        Coord newCoord = calculateNewCoord(event.getCoord());
        dragRegion.setCoord(newCoord);
    }

    private Coord calculateNewCoord(Coord mouse){
        return new Coord(mouse.x() - initialDragOffset.x(), mouse.y() - initialDragOffset.y());
    }


    @Override
    protected void onDragRelease(MouseInputEvent event){}

}
