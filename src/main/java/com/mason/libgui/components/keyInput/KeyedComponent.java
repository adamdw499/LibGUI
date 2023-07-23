package com.mason.libgui.components.keyInput;

import com.mason.libgui.components.misc.ClickOffable;
import com.mason.libgui.core.GUIManager;
import com.mason.libgui.core.UIComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * An example implementation of a UI component accepting key input.
 */
public abstract class KeyedComponent extends UIComponent implements ClickOffable{


    /**
     * isClicked: Whether the component is "clicked on" currently.
     * keys: the key handler.
     */
    protected boolean isClicked = false;
    protected KeyBuffer keys;


    /**
     * Creates an instance, leaving the initialisation of the keyHandler until later.
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public KeyedComponent(int x, int y, int w, int h){
        super(x, y, w, h);
    }

    /**
     * Creates an instance.
     * @param x
     * @param y
     * @param w
     * @param h
     * @param keys
     */
    public KeyedComponent(int x, int y, int w, int h, KeyBuffer keys){
        this(x, y, w, h);
        setKeyHandler(keys);
    }

    /**
     * Creates an instance, taking the key handler from the gui.
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gui
     */
    public KeyedComponent(int x, int y, int w, int h, GUIManager gui){
        this(x, y, w, h, gui.getKeyBuffer());
    }


    @Override
    public void clickOff(){
        isClicked = false;
    }

    public final void setKeyHandler(KeyBuffer keys){
        this.keys = keys;
    }

    /**
     * Sets the KeyHandler from the GUI
     * @param gui
     */
    public final void setKeyHandler(GUIManager gui){
        keys = gui.getKeyBuffer();
    }


    /**
     * Activates the "isClicked" quality.
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e){
        isClicked = true;
    }

    /**
     * Processes the key in the key handler.
     * @param mx
     * @param my
     */
    @Override
    public void tick(int mx, int my){
        if(isClicked && keys != null){
            if(keys.hasNewKeyTyped()){
                processKeyTyped(keys.popKeyTyped());
            }else if(keys.hasNewKeyPressed()){
                processKeyPressed(keys.popKeyPressed());
            }else if(keys.hasNewKeyReleased()){
                processKeyReleased(keys.popKeyReleased());
            }
        }
    }

    /**
     * Consumes a key typed event, if the KeyHandler is initialised and the component is clicked on.
     * @param e
     */
    protected abstract void processKeyTyped(KeyEvent e);

    /**
     * Consumes a key pressed event, if the KeyHandler is initialised and the component is clicked on.
     * @param e
     */
    protected abstract void processKeyPressed(KeyEvent e);

    /**
     * Consumes a key released event, if the KeyHandler is initialised and the component is clicked on.
     * @param e
     */
    protected abstract void processKeyReleased(KeyEvent e);


}
