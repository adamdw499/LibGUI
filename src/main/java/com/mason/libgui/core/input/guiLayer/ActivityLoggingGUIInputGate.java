package com.mason.libgui.core.input.guiLayer;


import com.mason.libgui.core.input.mouse.MouseInputEvent;

import java.awt.event.KeyEvent;

import static com.mason.libgui.utils.logging.ActivityLogger.ACTIVITY_LOGGER;

public class ActivityLoggingGUIInputGate extends SimpleGUIInputGate{


    public ActivityLoggingGUIInputGate(){
        super();
    }


    @Override
    public void keyTyped(KeyEvent e){
        ACTIVITY_LOGGER.keyTyped(e);
        super.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e){
        ACTIVITY_LOGGER.keyPressed(e);
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e){
        ACTIVITY_LOGGER.keyReleased(e);
        super.keyReleased(e);
    }

    @Override
    public void onMouseMoved(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMouseMoved(e);
    }

    @Override
    public void onMouseDragged(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMouseDragged(e);
    }

    @Override
    public void onMousePressed(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMousePressed(e);
    }

    @Override
    public void onMouseReleased(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMouseReleased(e);
    }

    @Override
    public void onMouseClicked(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMouseClicked(e);
    }

    @Override
    public void onMouseWheel(MouseInputEvent e){
        ACTIVITY_LOGGER.printMouseInputEvent(e);
        super.onMouseWheel(e);
    }

}
