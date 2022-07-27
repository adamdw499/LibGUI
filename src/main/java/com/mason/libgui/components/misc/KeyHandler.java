package com.mason.libgui.components.misc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{


    private boolean newKeyTyped = false, newKeyPressed = false, newKeyReleased = false;
    private KeyEvent currentKeyTyped, currentKeyPressed, currentKeyReleased;


    @Override
    public void keyTyped(KeyEvent e){
        newKeyTyped = true;
        currentKeyTyped = e;
    }

    @Override
    public void keyPressed(KeyEvent e){
        newKeyPressed = true;
        currentKeyPressed = e;
    }

    @Override
    public void keyReleased(KeyEvent e){
        newKeyReleased = true;
        currentKeyReleased = e;
    }


    public boolean hasNewKeyTyped(){
        return newKeyTyped;
    }

    public boolean hasNewKeyPressed(){
        return newKeyPressed;
    }

    public boolean hasNewKeyReleased(){
        return newKeyReleased;
    }

    public KeyEvent pollCurrentKeyTyped(){
        newKeyTyped = false;
        return currentKeyTyped;
    }

    public KeyEvent pollCurrentKeyPressed(){
        newKeyPressed = false;
        return currentKeyPressed;
    }

    public KeyEvent pollCurrentKeyReleased(){
        newKeyReleased = false;
        return currentKeyReleased;
    }

}
