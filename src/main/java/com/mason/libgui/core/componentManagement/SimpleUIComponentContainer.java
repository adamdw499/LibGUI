package com.mason.libgui.core.componentManagement;

import com.mason.libgui.core.component.UIComponent;

import java.util.*;

public class SimpleUIComponentContainer implements IterableUIComponentContainer{


    private final List<UIComponent> components;


    public SimpleUIComponentContainer(){
        components = new LinkedList<>();
    }


    @Override
    public Iterator<UIComponent> iterator(){
        return components.iterator();
    }


    @Override
    public void addComponent(UIComponent comp){
        components.add(comp);
    }


    public void addComponentToBack(UIComponent comp){
        components.add(0, comp);
    }

    @Override
    public void removeComponent(UIComponent comp){
        components.remove(comp);
    }

}
