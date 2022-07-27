
package com.mason.libgui.core;

import com.mason.libgui.components.misc.KeyHandler;
import com.mason.libgui.utils.ExceptionHandler;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Position;
import com.mason.libgui.utils.Utils;

/**
 *
 * @author Adam Whittaker
 */
public class GUIManager{
    
    
    private final Window window;
    private final UIComponentManager compManager;
    private final Pacemaker pacemaker;
    private ExceptionHandler exceptionHandler;
    private final KeyHandler keyHandler;
    
    
    public GUIManager(int width, int height, String title){
        this(width, height, title, Utils.DEFAULT_EXCEPTION_HANDLER);
    }

    public GUIManager(int width, int height, String title, ExceptionHandler ex){
        compManager = new UIComponentManager(StyleInfo.DEFAULT_STYLE_INFO, width, height);
        keyHandler = new KeyHandler();
        window = new Window(width, height, title, compManager, keyHandler);
        pacemaker = new Pacemaker(this);
        setExceptionHandler(ex);
    }
    
    
    protected void tick(){
        compManager.tick(-1, -1);
    }
    
    
    protected void render(){
        window.render(compManager);
    }

    public void start(){
        new Thread(pacemaker).start();
    }

    public void terminate(){
        pacemaker.terminate();
    }

    public void passException(Exception e){
        exceptionHandler.handleException(e);
    }


    public Window getWindow(){
        return window;
    }

    public Pacemaker getPacemaker(){
        return pacemaker;
    }

    public KeyHandler getKeyHandler(){
        return keyHandler;
    }

    public final void setExceptionHandler(ExceptionHandler ex){
        exceptionHandler = ex;
        ex.setParameters(this);
    }

    public ExceptionHandler getExceptionHandler(){
        return exceptionHandler;
    }
    
    
    public void addComponent(UIComponent comp){
        compManager.addComponent(comp);
    }

    public void addComponent(UIComponent comp, Position horizontal, Position vertical){
        compManager.addComponent(comp, horizontal, vertical);
    }
    
    public void removeComponent(UIComponent comp){
        compManager.removeComponent(comp);
    }
    
}
