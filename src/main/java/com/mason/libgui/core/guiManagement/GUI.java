
package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.componentManagement.UIComponentContainer;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.guiLayer.ActivityLoggingGUIInputGate;
import com.mason.libgui.core.input.guiLayer.GUIInputGate;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import com.mason.libgui.core.input.rawLayer.RawToGUIInputAdapter;
import com.mason.libgui.utils.structures.Size;

import java.awt.event.KeyListener;

public class GUI implements UIComponentContainer, GUIInputRegister<BoundedMouseInputListener>{


    private final Window window;
    private final UIComponentManager componentManager;
    private final RawInputSocket inputSocket;
    private final Pacemaker pacemaker;


    private GUI(Window window, UIComponentManager componentManager, RawInputSocket inputSocket){
        this.window = window;
        this.componentManager = componentManager;
        this.inputSocket = inputSocket;
        pacemaker = new Pacemaker(this::render, this::tick);
        window.registerInputSocket(this.inputSocket);
    }


    public static GUI buildSimpleGUI(Size size, String title){
        Window window = new Window(size, title);
        UIComponentManager componentManager = UIComponentManager.buildSimpleUIComponentManager();
        RawToGUIInputAdapter rawSocket = new RawToGUIInputAdapter();
        GUIInputGate guiInputGate = new ActivityLoggingGUIInputGate();

        rawSocket.setGUIInputSocket(guiInputGate);
        guiInputGate.setDelegateSocket(componentManager.getInputDistributor());

        return new GUI(window, componentManager, rawSocket);
    }

    public static GUI buildUnwiredGUI(Window window, UIComponentManager componentManager, RawInputSocket inputSocket){
        return new GUI(window, componentManager, inputSocket);
    }


    private void tick(){
        componentManager.tickComponents();
    }

    private void render(){
        window.render(componentManager);
    }


    public void start(){
        new Thread(pacemaker).start();
    }

    public void terminate(){
        pacemaker.terminate();
    }
    
    
    @Override
    public void addComponent(UIComponent comp){
        componentManager.addComponent(comp);
    }
    
    @Override
    public void removeComponent(UIComponent comp){
        componentManager.removeComponent(comp);
    }

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
    
}
