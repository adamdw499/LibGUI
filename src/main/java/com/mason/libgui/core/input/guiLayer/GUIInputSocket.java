package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface GUIInputSocket extends KeyListener, MouseInputListener{

    @Override
    default void keyTyped(KeyEvent e){}

    @Override
    default void keyPressed(KeyEvent e){}

    @Override
    default void keyReleased(KeyEvent e){}

}
