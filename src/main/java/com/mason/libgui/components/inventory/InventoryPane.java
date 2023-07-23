package com.mason.libgui.components.inventory;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.panes.Pane;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;

/**
 * Arranges buttons in a grid array.
 */
public class InventoryPane extends Pane{


    private int rows, columns, padding, rightSpace, leftSpace, topSpace, bottomSpace, buttonWidth, buttonHeight;


    /**
     * Creates an instance.
     * @param info
     * @param x
     * @param y
     * @param leftSpace The space between the left of the pane and the start of the buttons.
     * @param rightSpace The space between the right of the pane and the end of the buttons.
     * @param topSpace The space between the top of the pane and the end of the buttons.
     * @param botSpace The space between the bottom of the pane and the end of the buttons.
     * @param padding The space between buttons.
     * @param buttons
     * @param but_width The width of each button.
     * @param but_height The height of each button.
     * @param rows Number of rows.
     * @param cols Number of columns.
     */
    public InventoryPane(StyleInfo info, int x, int y, int leftSpace, int rightSpace,
                         int topSpace, int botSpace, int padding, Button[] buttons, int but_width, int but_height,
                         int rows, int cols){
        super(info, x, y,
                cols * (but_width + padding) - padding + leftSpace + rightSpace,
                rows * (but_height + padding) - padding + topSpace + botSpace);
        for(Button button : buttons) addComponent(button);
        this.rows = rows;
        this.columns = cols;
        this.padding = padding;
        this.rightSpace = rightSpace;
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.bottomSpace = botSpace;
        this.buttonWidth = but_width;
        this.buttonHeight = but_height;
        placeButtons(buttons);
    }

    /**
     * Creates an instance.
     * @param info
     * @param sideSpace The space between the horizontal edges of the pane and the buttons.
     * @param topSpace The space between the top of the pane and the end of the buttons.
     * @param botSpace The space between the bottom of the pane and the end of the buttons.
     * @param padding The space between buttons.
     * @param buttons
     * @param cols Number of columns.
     */
    public InventoryPane(StyleInfo info, int sideSpace,
                         int topSpace, int botSpace, int padding, Button[] buttons, int cols){
        this(info, 0, 0, sideSpace, sideSpace, topSpace, botSpace, padding, buttons, buttons[0].getWidth(),
                buttons[0].getHeight(), Math.ceilDiv(buttons.length, cols), cols);
    }

    /**
     * Creates an instance.
     * @param info
     * @param topSpace The space between the top of the pane and the end of the buttons.
     * @param botSpace The space between the bottom of the pane and the end of the buttons.
     * @param buttons
     * @param cols Number of columns.
     */
    public InventoryPane(StyleInfo info, int topSpace, int botSpace, Button[] buttons, int cols){
        this(info, 2*info.getLineWidth(), topSpace, botSpace, info.getLineWidth(), buttons, cols);
    }


    /**
     * Places the button positions.
     * @param buttons
     */
    protected void placeButtons(Button[] buttons){
        int x = leftSpace, y = topSpace, n = 0;
        for(Button button : buttons){
            button.setX(x);
            button.setY(y);
            n++;
            if(n % columns == 0){
                x = leftSpace;
                y += padding + buttonHeight;
            }else x += padding + buttonWidth;
        }
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(info.BACKGROUND);
        g.fillRect(x, y, width, height);
        super.render(g);
    }


}
