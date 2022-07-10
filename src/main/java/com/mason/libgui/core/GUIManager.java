
package com.mason.libgui.core;

/**
 *
 * @author Adam Whittaker
 */
public class GUIManager{
    
    
    private final Window window;
    private final UIComponentManager compManager;
    
    
    public GUIManager(int width, int height, String title){
        compManager = new UIComponentManager(width, height);
        window = new Window(width, height, title, compManager);
    }
    
    
    protected void tick(){
        compManager.tick(-1, -1);
    }
    
    
    protected void render(){
        window.render(compManager);
    }
    
    
    public void addComponent(UIComponent comp){
        compManager.addComponent(comp);
    }
    
    public void removeComponent(UIComponent comp){
        compManager.removeComponent(comp);
    }
    
}
