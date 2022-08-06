package com.mason.libgui.components.misc;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

public class Tooltip extends UIText{


    private StyleInfo info;


    public Tooltip(String t, StyleInfo st, boolean dropS, int dropShadowOff, Color shadowC, int x, int y, int w, int padding){
        super(t, st.FONT, st.TEXT_COLOR, dropS, dropShadowOff, shadowC, x, y, w, padding);
        info = st;
    }

    public Tooltip(String t, StyleInfo info, int x, int y, int w) {
        this(t, info, false, 2, info.TEXT_COLOR.darker().darker(), x, y, w, info.getLineWidth());
    }

    public Tooltip(String t, int x, int y, int w){
        this(t, StyleInfo.ALPHA_STYLE_INFO, x, y, w);
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(info.FORE_HIGHLIGHT);
        g.fillRect(x, y, width, height);
        super.render(g);
    }

}
