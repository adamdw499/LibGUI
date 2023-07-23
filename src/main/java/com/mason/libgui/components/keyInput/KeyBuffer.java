package com.mason.libgui.components.keyInput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/**
 * A buffer for key events for the GUIManager.
 */
public class KeyBuffer implements KeyListener{


    /**
     * Stores the latest key events.
     */
    private LinkedList<KeyEvent> typedKeys, pressedKeys, releasedKeys;


    /**
     * Stores the key event
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e){
        typedKeys.add(e);
    }

    /**
     * Stores the key event
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e){
        pressedKeys.add(e);
    }

    /**
     * Stores the key event
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e){
        releasedKeys.add(e);
    }


    /**
     * @return true if there is a new key typed event
     */
    public boolean hasNewKeyTyped(){
        return !typedKeys.isEmpty();
    }

    /**
     * @return true if there is a new key pressed event
     */
    public boolean hasNewKeyPressed(){
        return !pressedKeys.isEmpty();
    }

    /**
     * @return true if there is a new key released event
     */
    public boolean hasNewKeyReleased(){
        return !releasedKeys.isEmpty();
    }

    /**
     * @return the oldest unused type event.
     */
    public KeyEvent popKeyTyped(){
        return typedKeys.remove(0);
    }

    /**
     * @return the oldest unused pressed event.
     */
    public KeyEvent popKeyPressed(){
        return pressedKeys.remove(0);
    }

    /**
     * @return the oldest unused pop event.
     */
    public KeyEvent popKeyReleased(){
        return releasedKeys.remove(0);
    }

}
