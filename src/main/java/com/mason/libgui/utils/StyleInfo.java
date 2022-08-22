
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
    public Color TEXT_COLOR;
    
    public Font FONT;
    public Font TITLE_FONT;
    public RenderUtils RENDER_UTILS;
    
    
    public StyleInfo(Color fg, Color bg, Color b, Color fhl, Color t, Font f, Font tf, RenderUtils utils){
        FOREGROUND = fg;
        FONT = f;
        BACKGROUND = bg;
        BORDER = b;
        FORE_HIGHLIGHT = fhl;
        TEXT_COLOR = t;
        TITLE_FONT = tf;
        RENDER_UTILS = utils;
    }


    public int getLineWidth(){
        return RENDER_UTILS.getLineWidth();
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

    public static Color addAlpha(Color col, int alpha){
        return new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha);
    }
    

    public static final StyleInfo DEFAULT_STYLE_INFO;
    public static final StyleInfo ALTERNATE_STYLE_INFO_1;
    public static final StyleInfo ALTERNATE_STYLE_INFO_2;
    public static final StyleInfo ALPHA_STYLE_INFO;
    public static final StyleInfo TEXT_INPUT_INFO;
    public static final StyleInfo LOADING_STYLE_INFO;


    static{
        Color[] fore = analogousColors(new Color(75, 32, 32));
        Color darkGrey = new Color(42,42,42);
        Color grey = new Color(120, 120, 120);
        Color[] foreHighlight = analogousColors(new Color(105,62,62));
        Color[] fontCol = analogousColors(new Color(240,170,40));
        Font font = new Font("Dubstep Dungeons", Font.PLAIN, 18);
        Font title = new Font("Joystix Monospace", Font.PLAIN, 24);
        int alpha = 75;
        DefaultRenderUtils utils = new DefaultRenderUtils(6);

        ALTERNATE_STYLE_INFO_1 = new StyleInfo(fore[0], darkGrey, grey, foreHighlight[0], fontCol[0], font, title, utils);
        DEFAULT_STYLE_INFO = new StyleInfo(fore[1], darkGrey, grey, foreHighlight[1], fontCol[1], font, title, utils);
        ALTERNATE_STYLE_INFO_2 = new StyleInfo(fore[2], darkGrey, grey, foreHighlight[2], fontCol[2], font, title, utils);
        TEXT_INPUT_INFO = new StyleInfo(fore[1], new Color(40, 40, 60), grey, foreHighlight[1], new Color(220, 220, 180), font, title, utils);
        LOADING_STYLE_INFO = new StyleInfo(fore[1], darkGrey, grey, foreHighlight[1], Color.WHITE, font, title, utils);
        ALPHA_STYLE_INFO = new StyleInfo(
                addAlpha(fore[0], alpha),
                addAlpha(darkGrey, alpha),
                addAlpha(grey, alpha),
                addAlpha(foreHighlight[0], alpha),
                addAlpha(fontCol[0], alpha),
                new Font("Dubstep Dungeons", Font.PLAIN, 12),
                new Font("Joystix Monospace", Font.PLAIN, 16),
                utils
        );
    }
    
}
