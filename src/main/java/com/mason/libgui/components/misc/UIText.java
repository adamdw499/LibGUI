
package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Position;

import java.awt.*;
import java.util.LinkedList;

import static com.mason.libgui.utils.StyleInfo.DEFAULT_STYLE_INFO;
import static com.mason.libgui.utils.Utils.stringDimension;

/**
 *
 * @author Adam Whittaker
 */
public class UIText extends UIComponent{
    
    
    private final String[] text;
    private final Font font;
    private Color color;
    private boolean dropShadow;
    private final int dropShadowOffset;
    private Color shadowColor;
    private int padding;
    
    
    public UIText(String t, Font f, Color c, boolean dropS, int dropShadowOff, Color shadowC, int x, int y, int w, int pad){
        super(x, y, w, -1);
        padding = pad;
        font = f;
        text = splitLines(t);
        color = c;
        dropShadow = dropS;
        dropShadowOffset = dropShadowOff;
        shadowColor = shadowC;
        setHeight(text.length*(stringDimension("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm", font)[1] + padding) + padding);
    }
    
    public UIText(String t, StyleInfo info, int x, int y, int w, boolean title){
        this(t, title ? info.TITLE_FONT : info.FONT, info.TEXT_COLOR, true, 2,
                info.TEXT_COLOR.darker().darker(), x, y, w, info.getLineWidth());
    }
    
    public UIText(String t, StyleInfo info, int x, int y, int w){
        this(t, info, x, y, w, false);
    }

    public UIText(String t, StyleInfo info, int x, int y){
        this(t, info, x, y, stringDimension(t, info.FONT)[0] + 2*info.getLineWidth());
    }

    public UIText(String t, int x, int y){
        this(t, DEFAULT_STYLE_INFO, x, y);
    }
    

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
