package com.mason.libgui.components.dialogues;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.misc.UIText;
import com.mason.libgui.components.panes.DraggablePane;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.exceptions.OversizedComponentException;

import java.awt.*;
import java.util.Arrays;

import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;
import static com.mason.libgui.utils.RenderUtils.drawBorder;
import static java.lang.Math.max;

public class Dialogue extends DraggablePane{


    private UIText title;
    private UIText text;


    public Dialogue(int x, int y, int w, StyleInfo info, String title, String text, Button[] buttons, int padding){
        super(info, x, y, calcWidth(w, buttons, padding), -1);
        this.info = info;
        this.title = new UIText(title, info, padding, padding, width, true);
        this.text = new UIText(text, info, padding, 2 * padding + this.title.getHeight(), width, false);
        for(Button b : buttons){
            addComponent(b);
        }
        setHeight(positionButtons(buttons, padding));
        addComponent(this.title);
        addComponent(this.text);
    }

    public Dialogue(int x, int y, int w, int h, StyleInfo info, UIText title, UIText text, Button[] buttons){
        super(info, x, y, w, h);
        this.info = info;
        this.title = title;
        this.text = text;
        for(Button b : buttons){
            addComponent(b);
        }
        addComponent(this.title);
        addComponent(this.text);
    }

    public Dialogue(int x, int y, int w, String title, String text, Button[] buttons){
        this(x, y, w, StyleInfo.DEFAULT_STYLE_INFO, title, text, buttons, LINE_WIDTH);
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(info.BACKGROUND);
        g.fillRect(x, y, width, height);
        renderComponents(g);
        renderBorder(g);
    }


    private int positionButtons(Button[] buttons, int padding){
        int s = 3 * padding + this.title.getHeight() + this.text.getHeight();
        if(buttons.length == 0) return s + padding;

        int currentLineHeight = -1;
        int position = 2*padding;
        for(int n=0; n < buttons.length; n++){
            buttons[n].setX(position);
            buttons[n].setY(s);
            if(currentLineHeight < buttons[n].getHeight()) currentLineHeight = buttons[n].getHeight();
            if(n < buttons.length - 1){
                if(position + buttons[n+1].getWidth() + padding > width - padding){
                    if(position == 2 * padding) throw new OversizedComponentException(buttons[n+1], this);
                    position = 2 * padding;
                    s += currentLineHeight + padding;
                    currentLineHeight = -1;
                }else{
                    position += buttons[n].getWidth() + padding;
                }
            }else if(position + buttons[n].getWidth() + padding > width - padding){
                throw new OversizedComponentException(buttons[n], this);
            }
        }
        return s + currentLineHeight + 2*padding;
    }

    private static int calcWidth(int minWidth, Button[] buttons, int padding){
        return max(minWidth,
                Arrays.stream(buttons).mapToInt(b -> b.getWidth()).max().orElse(-4*padding) + 4*padding);
    }


    @Override
    public String toString(){
        return "[Dialogue] " + title.getText();
    }

}
