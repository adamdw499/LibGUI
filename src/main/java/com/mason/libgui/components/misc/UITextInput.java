package com.mason.libgui.components.misc;

import com.mason.libgui.components.keyInput.KeyBuffer;
import com.mason.libgui.components.keyInput.KeyedComponent;
import com.mason.libgui.core.GUIManager;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.Utils;
import com.mason.libgui.utils.vcs.MinorVCS;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.mason.libgui.utils.Utils.stringDimension;

/**
 * A component that accepts text input.
 */
public class UITextInput extends KeyedComponent{


    /**
     * TypeIndicator: The flashing line that indicates where the user is typing.
     * maxLength: The maximum length of the text.
     */
    private MinorVCS vcs = new MinorVCS("");
    private final TypeIndicator typeLine;
    private final StyleInfo info;
    private final int maxLength;


    /**
     * Creates an instance.
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     * @param keys The key input.
     * @param maxLength
     */
    public UITextInput(StyleInfo info, int x, int y, int w, int h, KeyBuffer keys, int maxLength){
        super(x, y, w, h, keys);
        this.info = info;
        this.maxLength = maxLength;
        typeLine = new TypeIndicator(y+info.getLineWidth()*2, 2,
                height-4*info.getLineWidth(), 60);
    }

    /**
     * Creates an instance with default style info. Takes the key buffer from the GUI.
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gui
     */
    public UITextInput(int x, int y, int w, int h, GUIManager gui, int maxLength){
        this(StyleInfo.TEXT_INPUT_INFO, x, y, w, h, gui.getKeyBuffer(), maxLength);
    }

    /**
     * Creates an instance with a maxLength of 12 and default style info. Takes the key buffer from the GUI.
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gui
     */
    public UITextInput(int x, int y, int w, int h, GUIManager gui){
        this(x, y, w, h, gui, 12);
    }


    @Override
    public void render(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, info, x, y, width, height);
        g.setColor(info.BACKGROUND);
        g.fillRect(x+info.getLineWidth(), y+info.getLineWidth(),
                width-2*info.getLineWidth(), height-2*info.getLineWidth());
        renderText(g);
    }

    /**
     * Types a character
     * @param e
     */
    @Override
    protected void processKeyTyped(KeyEvent e){
        if(vcs.length() < maxLength && Utils.isAlphanumeric(e.getKeyChar())){
            //vcs = vcs.substring(0, typeLine.position) + e.getKeyChar() + vcs.substring(typeLine.position);
            vcs.typeChar(e.getKeyChar(), typeLine.position);
            typeLine.setPosition(typeLine.position + 1, vcs);
        }
    }

    /**
     * Handles backspace, delete, left and right arrow buttons.
     * @param e
     */
    @Override
    protected void processKeyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT -> {
                if(typeLine.position > 0) typeLine.setPosition(typeLine.position-1, vcs);
            }
            case KeyEvent.VK_RIGHT -> {
                if(typeLine.position < vcs.length()) typeLine.setPosition(typeLine.position+1, vcs);
            }
            case KeyEvent.VK_BACK_SPACE -> {
                if(typeLine.position > 0){
                    //vcs = vcs.substring(0, typeLine.position-1) + vcs.substring(typeLine.position);
                    vcs.deleteChar(typeLine.position-1);
                    typeLine.setPosition(typeLine.position-1, vcs);
                }
            }
            case KeyEvent.VK_DELETE -> {
                if(typeLine.position < vcs.length()){
                    //vcs = vcs.substring(0, typeLine.position) + vcs.substring(typeLine.position+1);
                    vcs.deleteChar(typeLine.position);
                }
            }
            case KeyEvent.VK_Z -> {
                if(e.isControlDown()){
                    vcs.rollBack();
                    typeLine.verifyPositionAtMostLength(vcs);
                }
            }
            case KeyEvent.VK_Y -> {
                if(e.isControlDown()){
                    vcs.rollForward();
                    typeLine.verifyPositionAtMostLength(vcs);
                }
            }
        }
    }

    /**
     * Nothing happens on key release.
     * @param e
     */
    @Override
    protected void processKeyReleased(KeyEvent e){}

    /**
     * Ticks the object and the typeLine.
     * @param mx
     * @param my
     */
    @Override
    public void tick(int mx, int my){
        typeLine.tick(mx, my);
        super.tick(mx, my);
    }

    /**
     * Renders the text.
     * @param g
     */
    public void renderText(Graphics2D g){
        g.setFont(info.FONT);
        FontMetrics f = g.getFontMetrics();
        g.setColor(info.TEXT_COLOR);
        if(isClicked){
            g.drawString(vcs.substring(0, typeLine.position), x + 2*info.getLineWidth(),
                    y + info.getLineWidth() + f.getHeight());
            g.drawString(vcs.substring(typeLine.position), typeLine.getX() +
                    typeLine.getWidth() + (int)(info.getLineWidth()*0.5D),
                    y + info.getLineWidth() + f.getHeight());
            typeLine.render(g);
        }else{
            g.drawString(vcs.getText(), x + 2*info.getLineWidth(),
                    y + info.getLineWidth() + f.getHeight());
        }
    }


    public String getText(){
        return vcs.getText();
    }

    @Override
    public String toString(){
        return "[UITextInput] " + getText();
    }


    /**
     * The type line indicator object.
     */
    private class TypeIndicator extends UIComponent{


        /**
         * position: The index of the string where the user is currently typing.
         * tickDelta: A timer for whether the type marker is visible.
         * switchTime: How long each visible cycle takes for the type marker.
         * on: Whether the type marker is visible.
         * color: The color of the type marker.
         */
        protected int position = 0;
        private int tickDelta;
        private final int switchTime;
        private boolean on = false;
        private final Color color = new Color(180, 180, 180);


        /**
         * Creates an instance.
         * @param y
         * @param w
         * @param h
         * @param switchTime
         */
        public TypeIndicator(int y, int w, int h, int switchTime){
            super(0, y, w, h);
            this.switchTime = switchTime;
            recalcX("");
        }


        /**
         * Sets the position on the given string.
         * @param pos
         * @param vcs
         */
        protected void setPosition(int pos, MinorVCS vcs){
            position = pos;
            recalcX(vcs.substring(0,pos));
        }

        /**
         * Recalculates the x coordinate of the type marker.
         * @param before The string located before the type marker.
         */
        private void recalcX(String before){
            x = (int)(2.5*info.getLineWidth()) + stringDimension(before, info.FONT)[0] + UITextInput.this.x;
        }

        /**
         * Checks that the type line hasn't moved too far to the right.
         * @param vcs
         */
        private void verifyPositionAtMostLength(MinorVCS vcs){
            if(typeLine.position > vcs.length()) typeLine.setPosition(vcs.length(), vcs);
        }

        @Override
        public void render(Graphics2D g){
            if(on){
                g.setColor(color);
                g.fillRect(x, y, width, height);
            }
        }

        /**
         * Increments the tickDelta and flips the on state if necessary.
         * @param mx
         * @param my
         */
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
