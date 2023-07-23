package com.mason.libgui.components.misc;

import com.mason.libgui.components.keyInput.KeyBuffer;
import com.mason.libgui.components.keyInput.KeyedComponent;
import com.mason.libgui.core.GUIManager;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.mason.libgui.utils.Utils.stringDimension;

/**
 *
 */
public class UITextInput extends KeyedComponent{


    private String text = "";
    private final TypeIndicator typeLine;
    private final StyleInfo info;
    private final int maxLength;


    public UITextInput(StyleInfo info, int x, int y, int w, int h, KeyBuffer keys, int maxLength){
        super(x, y, w, h);
        this.keys = keys;
        this.info = info;
        this.maxLength = maxLength;
        typeLine = new TypeIndicator(y+info.getLineWidth()*2, 2,
                height-4*info.getLineWidth(), 60);
    }

    public UITextInput(int x, int y, int w, int h, GUIManager gui){
        this(StyleInfo.TEXT_INPUT_INFO, x, y, w, h, gui.getKeyBuffer(), 12);
    }


    @Override
    public void render(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, info, x, y, width, height);
        g.setColor(info.BACKGROUND);
        g.fillRect(x+info.getLineWidth(), y+info.getLineWidth(),
                width-2*info.getLineWidth(), height-2*info.getLineWidth());
        renderText(g);
    }

    @Override
    protected void processKeyTyped(KeyEvent e){
        if(text.length() < maxLength && Utils.isAlphanumeric(e.getKeyChar())){
            text = text.substring(0, typeLine.position) + e.getKeyChar() + text.substring(typeLine.position);
            typeLine.setPosition(typeLine.position + 1, text);
        }
    }

    @Override
    protected void processKeyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT -> {
                if(typeLine.position > 0) typeLine.setPosition(typeLine.position-1, text);
            }
            case KeyEvent.VK_RIGHT -> {
                if(typeLine.position < text.length()) typeLine.setPosition(typeLine.position+1, text);
            }
            case KeyEvent.VK_BACK_SPACE -> {
                if(typeLine.position > 0){
                    text = text.substring(0, typeLine.position-1) + text.substring(typeLine.position);
                    typeLine.setPosition(typeLine.position-1, text);
                }
            }
            case KeyEvent.VK_DELETE -> {
                if(typeLine.position < text.length()){
                    text = text.substring(0, typeLine.position) + text.substring(typeLine.position+1);
                }
            }
        }
    }

    @Override
    protected void processKeyReleased(KeyEvent e){}

    @Override
    public void tick(int mx, int my){
        typeLine.tick(mx, my);
        super.tick(mx, my);
    }

    public void renderText(Graphics2D g){
        g.setFont(info.FONT);
        FontMetrics f = g.getFontMetrics();
        g.setColor(info.TEXT_COLOR);
        if(isClicked){
            g.drawString(text.substring(0, typeLine.position), x + 2*info.getLineWidth(),
                    y + info.getLineWidth() + f.getHeight());
            g.drawString(text.substring(typeLine.position), typeLine.getX() +
                    typeLine.getWidth() + (int)(info.getLineWidth()*0.5D),
                    y + info.getLineWidth() + f.getHeight());
            typeLine.render(g);
        }else{
            g.drawString(text, x + 2*info.getLineWidth(),
                    y + info.getLineWidth() + f.getHeight());
        }
    }


    public String getText(){
        return text;
    }

    @Override
    public String toString(){
        return "[UITextInput] " + getText();
    }


    private class TypeIndicator extends UIComponent{


        protected int position;
        private int tickDelta;
        private final int switchTime;
        private boolean on = false;
        private final Color color = new Color(180, 180, 180);


        public TypeIndicator(int y, int w, int h, int switchTime){
            super(0, y, w, h);
            this.switchTime = switchTime;
            setPosition(0, "");
        }


        protected void setPosition(int pos, String str){
            position = pos;
            recalcX(str.substring(0,pos));
        }

        private void recalcX(String before){
            x = (int)(2.5*info.getLineWidth()) + stringDimension(before, info.FONT)[0] + UITextInput.this.x;
        }

        @Override
        public void render(Graphics2D g){
            if(on){
                g.setColor(color);
                g.fillRect(x, y, width, height);
            }
        }

        @Override
        public void tick(int mx, int my){
            tickDelta++;
            if(tickDelta >= switchTime){
                tickDelta = 0;
                on = !on;
            }
        }

        @Override
        public String toString(){
            return "[TypeIndicator]";
        }

    }

}
