
package com.mason.libgui.core.componentManagement;

import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.input.componentLayer.GUIInputDistributor;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.componentLayer.UIComponentManagerInputDistributor;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;

import java.awt.*;
import java.awt.event.KeyListener;


public class UIComponentManager implements GUIInputRegister<BoundedMouseInputListener>, UIComponentContainer{
    
    
    private final IterableUIComponentContainer components;
    private final GUIInputDistributor<BoundedMouseInputListener> inputDistributor;


    private UIComponentManager(IterableUIComponentContainer components, GUIInputDistributor<BoundedMouseInputListener> inputDistributor){
        this.components = components;
        this.inputDistributor = inputDistributor;
    }

    public static UIComponentManager buildUIComponentManager(IterableUIComponentContainer components, GUIInputDistributor<BoundedMouseInputListener> inputDistributor){
        return new UIComponentManager(components, inputDistributor);
    }

    public static UIComponentManager buildSimpleUIComponentManager(){
        return new UIComponentManager(new SimpleUIComponentContainer(), new UIComponentManagerInputDistributor());
    }


    public GUIInputDistributor<BoundedMouseInputListener> getInputDistributor(){
        return inputDistributor;
    }


    //Container
    @Override
    public void addComponent(UIComponent comp){
        components.addComponent(comp);
    }
    
    public void removeComponent(UIComponent comp){
        components.removeComponent(comp);
    }
    

    //Ticking and Rendering
    public void tickComponents(){
        components.forEach(UIComponent::tick);
    }

    public void renderComponents(Graphics2D g){
        components.forEach(c -> c.render(g));
    }


    //Input
    @Override
    public void addMouseInputListener(BoundedMouseInputListener listener){
        inputDistributor.addMouseInputListener(listener);
    }

    @Override
    public void removeMouseInputListener(BoundedMouseInputListener listener){
        inputDistributor.removeMouseInputListener(listener);
    }

    @Override
    public void addKeyListener(KeyListener listener){
        inputDistributor.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(KeyListener listener){
        inputDistributor.removeKeyListener(listener);
    }

    
}
