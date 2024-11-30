package com.mason.libgui.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SpawningManager{


    private final List<UIComponent> components;
    private final HashMap<UIComponent, Boolean> spawning = new HashMap<>();
    private final List<UIComponent> despawning = new LinkedList<>();


    public SpawningManager(List<UIComponent> comp){
        components = comp;
    }


    public void tick(){
        if(!spawning.isEmpty()){
            spawning.forEach((comp, foreground) -> {
                if(foreground) components.add(0, comp);
                else components.add(comp);
            });
            spawning.clear();
        }
        if(!despawning.isEmpty()){
            components.removeAll(despawning);
            despawning.clear();
        }
    }

    public void addComponent(UIComponent comp, boolean foreground){
        spawning.put(comp, foreground);
    }

    public void addComponent(UIComponent comp){
        addComponent(comp, true);
    }

    public void addToBackground(UIComponent comp){
        addComponent(comp, false);
    }

    public void removeComponent(UIComponent comp){
        despawning.add(comp);
    }

}
