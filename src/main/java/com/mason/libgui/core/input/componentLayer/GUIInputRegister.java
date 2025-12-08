package com.mason.libgui.core.input.componentLayer;

import com.mason.libgui.core.input.mouse.MouseInputListener;

import java.awt.event.KeyListener;

public interface GUIInputRegister<E extends MouseInputListener>{

    void addKeyListener(KeyListener listener);
    void removeKeyListener(KeyListener listener);
    void addMouseInputListener(E listener);
    void removeMouseInputListener(E listener);

}
