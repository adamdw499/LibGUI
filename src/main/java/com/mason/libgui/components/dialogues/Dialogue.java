package com.mason.libgui.components.dialogues;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.misc.UIText;
import com.mason.libgui.components.panes.DraggablePane;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.boxPacking.BoxPacker;
import com.mason.libgui.utils.boxPacking.CenterAlignedBoxPacker;

import java.awt.*;
import java.util.Arrays;

import static com.mason.libgui.utils.StyleInfo.DEFAULT_STYLE_INFO;
import static java.lang.Math.max;

/**
 * Packages button options into a menu with a title and text.
 */
public class Dialogue extends DraggablePane{


    private UIText title;
    private UIText text;


    /**
     * Creates an instance. Uses the CenterAlignedBoxPacker.
     * @param x
     * @param y
     * @param w the minimum width
     * @param info
     * @param title
     * @param text
     * @param buttons
     * @param padding The space between buttons.
     */
    public Dialogue(int x, int y, int w, StyleInfo info, String title, String text, Button[] buttons, int padding){
        this(x, y, w, info, title, text, buttons, padding, new CenterAlignedBoxPacker(padding));
    }

    /**
     * Creates an instance.
     * @param x
     * @param y
     * @param w
     * @param info
     * @param title
     * @param text
     * @param buttons
     * @param padding
     * @param packer The box packing algorithm to place the buttons with.
     */
    public Dialogue(int x, int y, int w, StyleInfo info, String title, String text, Button[] buttons, int padding, BoxPacker packer){
        super(info, x, y, calcWidth(w, buttons, padding), -1);
        this.info = info;
        this.title = new UIText(title, info, padding, padding, width, true);
        this.text = new UIText(text, info, padding, 2 * padding + this.title.getHeight(), width, false);
        for(Button b : buttons){
            addComponent(b);
        }
        positionButtons(buttons, padding, packer);
        setHeight(calcHeight(buttons, padding));
        addComponent(this.title);
        addComponent(this.text);
    }

    /**
     * Creates an instance, allowing for custom positioned title and text. Uses the CenterAlignedBoxPacker.
     * @param x
     * @param y
     * @param w
     * @param h
     * @param info
     * @param title
     * @param text
     * @param buttons
     */
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

    /**
     * Creates an instance. Uses the CenterAlignedBoxPacker. Uses the default style info.
     * @param x
     * @param y
     * @param w
     * @param title
     * @param text
     * @param buttons
     */
    public Dialogue(int x, int y, int w, String title, String text, Button[] buttons){
        this(x, y, w, DEFAULT_STYLE_INFO, title, text, buttons, DEFAULT_STYLE_INFO.getLineWidth());
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(info.BACKGROUND);
        g.fillRect(x, y, width, height);
        renderComponents(g);
        renderBorder(g);
    }


    /**
     * Positions the buttons on the pane.
     * @param buttons
     * @param padding Space between buttons
     * @param packer Box packing algorithm.
     */
    private void positionButtons(Button[] buttons, int padding, BoxPacker packer){
        packer.pack(0, 3 * padding + this.title.getHeight() + this.text.getHeight(),
                width, Integer.MAX_VALUE, buttons);
    }

    /**
     * Calculates the width of the dialogue to be created.
     * @param minWidth the minimum width
     * @param buttons
     * @param padding the space between buttons.
     * @return
     */
    private static int calcWidth(int minWidth, Button[] buttons, int padding){
        return max(minWidth,
                Arrays.stream(buttons).mapToInt(UIComponent::getWidth).max().orElse(-4*padding) + 4*padding);
    }

    /**
     * Calculates the height of the dialogue to be created.
     * @param buttons
     * @param padding space between buttons.
     * @return
     */
    private static int calcHeight(Button[] buttons, int padding){
        return Arrays.stream(buttons).mapToInt(b -> b.getHeight() + b.getY()).max().orElse(0) + 2*padding;
    }


    @Override
    public String toString(){
        return "[Dialogue] " + title.getText();
    }

}
