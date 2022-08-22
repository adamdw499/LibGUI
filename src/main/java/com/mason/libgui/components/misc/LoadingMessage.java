package com.mason.libgui.components.misc;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

public class LoadingMessage extends LoadingCircle{


    private UIText text;
    private final int padding;
    private final StyleInfo info;


    public LoadingMessage(Color col, StyleInfo info, int x, int y, int diam, double angularVel, int gapHeight, int lineWidth, int padding){
        super(col, x, y, diam, angularVel, gapHeight, lineWidth);
        this.padding = padding;
        this.info = info;
        setMessage("");
    }

    public LoadingMessage(Color col, StyleInfo info, int x, int y, int diam){
        this(col, info, x, y, diam, 0.05, 2*info.getLineWidth(), info.getLineWidth(), info.getLineWidth());
    }

    public LoadingMessage(Color col, int x, int y, int diam){
        this(col, StyleInfo.LOADING_STYLE_INFO, x, y, diam);
    }


    public void setMessage(String message){
        text = new UIText(message, info, -1, y + padding + height);
        text.setX(x + width/2 - text.getWidth()/2);
    }

    @Override
    public void render(Graphics2D g){
        super.render(g);
        text.render(g);
    }

}
