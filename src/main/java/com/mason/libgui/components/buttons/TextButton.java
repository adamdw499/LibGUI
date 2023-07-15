package com.mason.libgui.components.buttons;

import com.mason.libgui.components.misc.UIText;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A Button with text
 */
public abstract class TextButton extends Button{


    /**
     * The text object
     */
    private final UIText text;


    /**
     * Wraps the text in a button barely enclosing the text.
     * @param text
     * @param info
     */
    public TextButton(UIText text, StyleInfo info){
        super(info, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        this.text = text;
    }

    /**
     * Creates a button with the UIText in the middle.
     * @param text
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public TextButton(UIText text, StyleInfo info, int x, int y, int w, int h){
        super(info, x, y, w, h);
        this.text = text;
        setX(x);
        setY(y);
    }

    /**
     * Creates a button with the UIText in the middle.
     * @param text
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public TextButton(String text, StyleInfo info, int x, int y, int w, int h){
        this(new UIText(text, info, -1, -1), info, x, y, w, h);
    }

    /**
     * Creates a button with the UIText in the middle.
     * @param text
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public TextButton(String text, int x, int y, int w, int h){
        this(text, StyleInfo.DEFAULT_STYLE_INFO, x, y, w, h);
    }

    /**
     * Wraps the text in a button barely enclosing the text.
     * @param text
     */
    public TextButton(String text, int x, int y){
        this(new UIText(text, x, y), StyleInfo.DEFAULT_STYLE_INFO);
    }


    @Override
    public void render(Graphics2D g){
        super.render(g);
        text.render(g);
    }


    @Override
    public void setX(int _x){
        super.setX(_x);
        text.setX(_x + width/2 - text.getWidth()/2);
    }

    @Override
    public void setY(int _y){
        super.setY(_y);
        text.setY(_y + height/2 - text.getHeight()/2);
    }


    @Override
    public String toString(){
        return "[TextButton] " + text.getText();
    }


    public static TextButton getBlankButton(String text, int x, int y, int w, int h){
        return new TextButton(text, x, y, w, h){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

    public static TextButton getBlankButton(String text, int x, int y){
        return new TextButton(text, x, y){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

}
