package com.mason.libgui.core.input.rawLayer;

import com.mason.libgui.core.input.guiLayer.GUIInputSocket;
import com.mason.libgui.core.input.mouse.MouseInputEvent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class RawToGUIInputAdapter implements RawInputSocket{


    private GUIInputSocket guiSocket;


    public RawToGUIInputAdapter(){

    }


    public void setGUIInputSocket(GUIInputSocket socket){
        guiSocket = socket;
    }

    @Override
    public void keyTyped(KeyEvent e){
        guiSocket.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e){
        guiSocket.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e){
        guiSocket.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        guiSocket.onMouseClicked(new MouseInputEvent(e));
    }

    @Override
    public void mousePressed(MouseEvent e){
        guiSocket.onMousePressed(new MouseInputEvent(e));
    }

    @Override
    public void mouseReleased(MouseEvent e){
        guiSocket.onMouseReleased(new MouseInputEvent(e));
    }

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}

    @Override
    public void mouseDragged(MouseEvent e){
        guiSocket.onMouseDragged(new MouseInputEvent(e));
    }

    @Override
    public void mouseMoved(MouseEvent e){
        guiSocket.onMouseMoved(new MouseInputEvent(e));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        guiSocket.onMouseWheel(new MouseInputEvent(e));
    }

}
