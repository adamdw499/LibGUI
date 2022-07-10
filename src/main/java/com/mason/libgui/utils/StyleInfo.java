
package com.mason.libgui.utils;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Adam Whittaker
 */
public class StyleInfo{
    
    
    public Color FOREGROUND;
    public Color BACKGROUND;
    public Color BORDER;
    public Color FORE_HIGHLIGHT;
    public Color TEXT;
    
    public Font FONT;
    
    
    public StyleInfo(Color fg, Color bg, Color b, Color fhl, Color t, Font f){
        FOREGROUND = fg;
        FONT = f;
        BACKGROUND = bg;
        BORDER = b;
        FORE_HIGHLIGHT = fhl;
        TEXT = t;
    }
    
    
    public static Color[] analogousColors(Color col, float delta){
        float[] vals = new float[3];
        vals = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), vals);
        float minus = vals[0] - delta;
        float plus = vals[0] + delta;
        return new Color[]{Color.getHSBColor(minus, vals[1], vals[2]),
            col, Color.getHSBColor(plus, vals[1], vals[2])};
    }
    
    public static Color[] analogousColors(Color col){
        return analogousColors(col, 1f/7f);
    }
    
    
    public static final StyleInfo DEFAULT_STYLE_INFO = new StyleInfo(
            new Color(75,32,32),
            new Color(42,42,42),
            new Color(120,120,120),
            new Color(105,62,62),
            new Color(240,170,40),
            //new Font("Joystix Monospace", Font.PLAIN, 24)
            new Font("Dubstep Dungeons", Font.PLAIN, 24)
    );
    
}
