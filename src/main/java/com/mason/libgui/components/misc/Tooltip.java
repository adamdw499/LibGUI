package com.mason.libgui.components.misc;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

/**
 * A simple tooltip.
 */
public class Tooltip extends UIText{


    private StyleInfo info;


    /**
     * Creates an instance by specifying all parameters of the underlying UIText object. The font and color is taken
     * from the style info.
     * @param t
     * @param st
     * @param dropS
     * @param dropShadowOff
     * @param shadowC
     * @param x
     * @param y
     * @param w
     * @param padding
     */
    public Tooltip(String t, StyleInfo st, boolean dropS, int dropShadowOff, Color shadowC, int x, int y, int w, int padding){
        super(t, st.FONT, st.TEXT_COLOR, dropS, dropShadowOff, shadowC, x, y, w, padding);
        info = st;
    }

    /**
     * Creates an instance with no drop shadow.
     * @param t
     * @param info
     * @param x
     * @param y
     * @param w
     */
    public Tooltip(String t, StyleInfo info, int x, int y, int w){
        this(t, info, false, 2, info.TEXT_COLOR.darker().darker(), x, y, w, info.getLineWidth());
    }

    /**
     * Creates an instance with no drop shadow, and the alpha style info.
     * @param t
     * @param x
     * @param y
     * @param w
     */
    public Tooltip(String t, int x, int y, int w){
        this(t, StyleInfo.ALPHA_STYLE_INFO, x, y, w);
    }

    /**
     * Creates an instance with no drop shadow, and the alpha style info. Calculates the width to be minimal.
     * @param t
     * @param x
     * @param y
     */
    public Tooltip(String t, int x, int y){
        super(t, StyleInfo.ALPHA_STYLE_INFO, x, y);
        info = StyleInfo.ALPHA_STYLE_INFO;
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(info.FORE_HIGHLIGHT);
        g.fillRect(x, y, width, height);
        super.render(g);
    }

}
