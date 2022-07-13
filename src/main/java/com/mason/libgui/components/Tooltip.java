package com.mason.libgui.components;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;

public class Tooltip extends UIText{


    private StyleInfo style;


    public Tooltip(String t, StyleInfo st, boolean dropS, int dropShadowOff, Color shadowC, int x, int y, int w, int h, int padding) {
        super(t, st.FONT, st.TEXT, dropS, dropShadowOff, shadowC, x, y, padding);
        style = st;
        width = w;
        height = h;
    }

    public Tooltip(String t, StyleInfo style, int x, int y, int w, int h) {
        this(t, style, false, 2, style.TEXT.darker().darker(), x, y, w, h, LINE_WIDTH);
    }

    public Tooltip(String t, int x, int y, int w, int h){
        this(t, StyleInfo.ALPHA_STYLE_INFO, x, y, w, h);
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(style.FORE_HIGHLIGHT);
        g.fillRect(x, y, width, height);
        super.render(g);
    }

}
