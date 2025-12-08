
package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.GraphicsTransformBehaviour;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.UIComponentContainer;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.InputDelegator;

import java.awt.*;
import java.awt.event.KeyListener;

public class Pane extends UIComponent implements UIComponentContainer, GUIInputRegister<BoundedMouseInputListener>, InputDelegator{


    private final UIComponentManager componentManager;
    private final PaneGUIInputTranslator inputTranslator;
    private final GraphicsTransformBehaviour graphicsTransform;


    protected Pane(HitboxRect boundary){
        this(boundary, UIComponentManager.buildSimpleUIComponentManager());
    }

    protected Pane(HitboxRect boundary, UIComponentManager componentManager){
        super(boundary);
        this.componentManager = componentManager;
        inputTranslator = new PaneGUIInputTranslator(boundary);
        inputTranslator.setDelegateSocket(componentManager.getInputDistributor());

        PaneGraphicsTransformBuilder transformBuilder =
                new PaneGraphicsTransformBuilder(this::renderAfterTranslation, boundary);
        graphicsTransform = transformBuilder.build();
    }

    public static Pane buildDefault(HitboxRect boundary){
        return new Pane(boundary);
    }

    public static Pane build(HitboxRect boundary, UIComponentManager componentManager){
        return new Pane(boundary, componentManager);
    }


    //Rendering relativisation
    @Override
    public void render(Graphics2D g){
        graphicsTransform.transformAndRender(g);
    }

    protected void renderAfterTranslation(Graphics2D g){
        componentManager.renderComponents(g);
    }


    //Ticking
    @Override
    public void tick(){
        componentManager.tickComponents();
    }


    @Override
    public void addComponent(UIComponent comp){
        componentManager.addComponent(comp);
    }

    @Override
    public void removeComponent(UIComponent comp){
        componentManager.removeComponent(comp);
    }


    //Input Register
    @Override
    public void addKeyListener(KeyListener listener){
        componentManager.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(KeyListener listener){
        componentManager.removeKeyListener(listener);
    }

    @Override
    public void addMouseInputListener(BoundedMouseInputListener listener){
        componentManager.addMouseInputListener(listener);
    }

    @Override
    public void removeMouseInputListener(BoundedMouseInputListener listener){
        componentManager.removeMouseInputListener(listener);
    }

    @Override
    public void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputRegister){
        inputRegister.addKeyListener(inputTranslator);
        inputRegister.addMouseInputListener(inputTranslator);
    }

    @Override
    public void unsetInputSource(GUIInputRegister<BoundedMouseInputListener> inputRegister){
        inputRegister.removeKeyListener(inputTranslator);
        inputRegister.removeMouseInputListener(inputTranslator);
    }

}
