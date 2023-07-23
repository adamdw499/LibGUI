
package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.util.LinkedList;

import static com.mason.libgui.utils.StyleInfo.DEFAULT_STYLE_INFO;
import static com.mason.libgui.utils.Utils.stringDimension;
import static java.lang.Math.max;

/**
 * Text to display
 * @author Adam Whittaker
 */
public class UIText extends UIComponent{
    
    
    private String[] text;
    private final Font font;
    private Color color;
    private boolean dropShadow;
    private final int dropShadowOffset;
    private Color shadowColor;
    private int padding;


    /**
     * Creates an instance
     * @param t The text
     * @param f the font
     * @param c color
     * @param dropS whether to have a drop shadow, giving a 3D effect.
     * @param dropShadowOffset the offset between the text and the shadow (the depth of the text). Can be set to -1
     *                      safely if drop shadows are always going to be off.
     * @param shadowC The color of the shadow.
     * @param x
     * @param y
     * @param w The maximum width of the text. The height is calculated based on the width.
     * @param pad The padding between each line of text.
     */
    public UIText(String t, Font f, Color c, boolean dropS, int dropShadowOffset, Color shadowC, int x, int y, int w, int pad){
        super(x, y, w, -1);
        padding = pad;
        font = f;
        recalcLinesAndHeight(t);
        color = c;
        dropShadow = dropS;
        this.dropShadowOffset = dropShadowOffset;
        shadowColor = shadowC;
    }

    /**
     * Creates a default instance, with a drop shadow. Font, color, shadow color are taken from a given style info.
     * Drop shadow offset is defaulted to 2.
     * @param t
     * @param info
     * @param x
     * @param y
     * @param w
     * @param title Whether the text is a title or not.
     */
    public UIText(String t, StyleInfo info, int x, int y, int w, boolean title){
        this(t, title ? info.TITLE_FONT : info.FONT, info.TEXT_COLOR, true, 2,
                info.TEXT_COLOR.darker().darker(), x, y, w, info.getLineWidth());
    }

    /**
     * Creates a default instance, with a drop shadow. Font, color, shadow color are taken from a given style info.
     * Drop shadow offset is defaulted to 2. Assumes the text is not a title.
     * @param t
     * @param info
     * @param x
     * @param y
     * @param w
     */
    public UIText(String t, StyleInfo info, int x, int y, int w){
        this(t, info, x, y, w, false);
    }

    /**
     * Creates a default instance, with a drop shadow. Font, color, shadow color are taken from a given style info.
     * Drop shadow offset is defaulted to 2. Assumes the text is not a title.
     * Width is calculated so the text is all in one line.
     * @param t
     * @param info
     * @param x
     * @param y
     */
    public UIText(String t, StyleInfo info, int x, int y){
        this(t, info, x, y, calcMinWidth(t, info.FONT, info.getLineWidth()));
    }

    /**
     * Creates a default instance, with a drop shadow. Font, color, shadow color are taken from the default style info.
     * Drop shadow offset is defaulted to 2. Assumes the text is not a title.
     * Width is calculated so the text is all in one line.
     * @param t
     * @param x
     * @param y
     */
    public UIText(String t, int x, int y){
        this(t, DEFAULT_STYLE_INFO, x, y);
    }


    /**
     * Splits the text into lines based on the space character so that they each fit into the width, or are one word.
     * @param text
     * @return
     */
    private String[] splitLines(String text){
        String[] words = text.split(" ");
        if(words.length == 1) return words;

        LinkedList<String> lines = new LinkedList<>();
        int x;
        StringBuilder currentLine = new StringBuilder(words[0]);
        for(int n=1; n < words.length; n++){
            if(stringDimension(currentLine + " " + words[n], font)[0] > width - 2*padding){
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(words[n]);
            }else{
                currentLine.append(" ").append(words[n]);
            }
        }
        lines.add(currentLine.toString());
        return lines.toArray(new String[0]);
    }

    /**
     * Calculates the minimum width the UIText object with given parameters can be.
     * @param str
     * @param font
     * @param padding
     * @return
     */
    private static int calcMinWidth(String str, Font font, int padding){
        return stringDimension(str, font)[0] + 2*padding;
    }

    /**
     * Calculates the minimum width the UIText object with given parameters can be.
     * @param strings The lines of text
     * @param font
     * @param padding
     * @return
     */
    private static int calcMinWidth(String[] strings, Font font, int padding){
        int ret = -1;
        for(String s : strings) ret = max(ret, calcMinWidth(s, font, padding));
        return ret;
    }

    /**
     * Sets the width of the text object to just
     */
    public void setToMinWidth(){
        width = calcMinWidth(text, font, padding);
    }

    /**
     * Resets the width. Recalculates height and text lines accordingly.
     * @param w The width.
     */
    @Override
    public void setWidth(int w){
        super.setWidth(w);
        StringBuilder t = new StringBuilder();
        for(String s : text) t.append(s).append(" ");
        recalcLinesAndHeight(t.substring(0, t.length()-1));
    }

    /**
     * Calculates the height of the text object based on the lines of the text, and sets the height to that value.
     * @param t
     */
    private void recalcLinesAndHeight(String t){
        text = splitLines(t);
        setHeight(text.length*(stringDimension("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm", font)[1] + padding) + padding);
    }


    @Override
    public String toString(){
        return "[UIText] " + getText();
    }

    public String getText(){
        StringBuilder ret = new StringBuilder();
        for(String line : text){
            ret.append(line).append(" ");
        }
        return ret.substring(0, ret.length()-1);
    }

    @Override
    public void render(Graphics2D g){
        g.setFont(font);
        FontMetrics f = g.getFontMetrics();
        int h = y + f.getHeight();
        for(String line : text){
            if(dropShadow){
                g.setColor(shadowColor);
                g.drawString(line, x + dropShadowOffset + padding, dropShadowOffset + h);
            }
            g.setColor(color);
            g.drawString(line, x + padding, h);

            h += stringDimension(line, font)[1];
        }
    }


    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

}
