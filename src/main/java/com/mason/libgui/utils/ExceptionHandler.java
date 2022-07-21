package com.mason.libgui.utils;

import com.mason.libgui.core.GUIManager;
import com.mason.libgui.core.Pacemaker;

import java.awt.*;

public abstract class ExceptionHandler{


    protected Window window;
    protected GUIManager gui;
    protected Pacemaker pacemaker;


    public ExceptionHandler(GUIManager g){
        setParameters(g);
    }

    public ExceptionHandler(){}


    public final void setParameters(GUIManager g){
        window = g.getWindow();
        gui = g;
        pacemaker = g.getPacemaker();
    }


    public abstract void handleException(Exception e);

}
