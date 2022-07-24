package com.mason.libgui.components.buttons;

import com.mason.libgui.components.misc.UIText;
import com.mason.libgui.utils.RenderUtils;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class TextButton extends Button{


    private UIText text;


    public TextButton(UIText text, StyleInfo info){
        super(info, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        this.text = text;
    }

    public TextButton(String text, StyleInfo info, int x, int y, int w){
        this(new UIText(text, info, x, y, w, false), info);
    }

    public TextButton(String text, int x, int y, int w){
        this(text, StyleInfo.DEFAULT_STYLE_INFO, x, y, w);
    }

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
        text.setX(_x);
    }

    @Override
    public void setY(int _y){
        super.setY(_y);
        text.setY(_y);
    }


    @Override
    public String toString(){
        return "[TextButton] " + text.getText();
    }


    public static TextButton getBlankButton(String text, int x, int y, int w){
        return new TextButton(text, x, y, w){

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
