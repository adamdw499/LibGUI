package com.mason.libgui.components.misc;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;

public class Tooltip extends UIText{


    private StyleInfo style;


    public Tooltip(String t, StyleInfo st, boolean dropS, int dropShadowOff, Color shadowC, int x, int y, int w, int padding){
        super(t, st.FONT, st.TEXT, dropS, dropShadowOff, shadowC, x, y, w, padding);
        style = st;
    }

    public Tooltip(String t, StyleInfo style, int x, int y, int w) {
        this(t, style, false, 2, style.TEXT.darker().darker(), x, y, w, LINE_WIDTH);
    }

    public Tooltip(String t, int x, int y, int w){
        this(t, StyleInfo.ALPHA_STYLE_INFO, x, y, w);
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(style.FORE_HIGHLIGHT);
        g.fillRect(x, y, width, height);
        super.render(g);
    }

}
