package com.mason.libgui.components.panes;

import com.mason.libgui.core.input.guiLayer.GUIMouseInputTransformer;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Coord;
import com.mason.libgui.utils.structures.RectQuery;

public class PaneGUIInputTranslator extends GUIMouseInputTransformer implements BoundedMouseInputListener{

    private final RectQuery boundary;


    PaneGUIInputTranslator(RectQuery boundary){
        super();
        this.boundary = boundary;
        setTransform(this::relativizeMouseInputEvent);
    }


    private MouseInputEvent relativizeMouseInputEvent(MouseInputEvent event){
        event.setCoordRelativeTo(boundary.getCoord());
        return event;
    }


    @Override
    public boolean withinBounds(Coord c){
        return boundary.withinBounds(c);
    }

}
