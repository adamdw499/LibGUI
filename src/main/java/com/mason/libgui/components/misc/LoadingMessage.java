package com.mason.libgui.components.misc;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

/**
 * A loading message with settable text and an accompanying loading circle animation.
 */
public class LoadingMessage extends LoadingCircleAnimation{


    private UIText text;
    private final int padding;
    private final StyleInfo info;


    /**
     * Creates an instance with an empty message, specifying all parameters of the loading circle.
     * @param col
     * @param info The style info for the text message to use.
     * @param x
     * @param y
     * @param diam
     * @param angularVel
     * @param gapHeight
     * @param lineWidth
     * @param padding
     */
    public LoadingMessage(Color col, StyleInfo info, int x, int y, int diam, double angularVel, int gapHeight, int lineWidth, int padding){
        super(col, x, y, diam, angularVel, gapHeight, lineWidth);
        this.padding = padding;
        this.info = info;
        setMessage("");
    }

    /**
     * Creates an instance with an empty message, using the given style info to create a loading circle with a default
     * angular velocity, gap height and line width.
     * @param col
     * @param info
     * @param x
     * @param y
     * @param diam
     */
    public LoadingMessage(Color col, StyleInfo info, int x, int y, int diam){
        this(col, info, x, y, diam, 0.05, 2*info.getLineWidth(), info.getLineWidth(), info.getLineWidth());
    }

    /**
     * Creates an instance with an empty message, using a default style info object.
     * @param col
     * @param x
     * @param y
     * @param diam
     */
    public LoadingMessage(Color col, int x, int y, int diam){
        this(col, StyleInfo.LOADING_STYLE_INFO, x, y, diam);
    }


    /**
     * Sets the message to be displayed under the loading circle.
     * @param message
     */
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
