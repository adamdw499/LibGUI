
package com.mason.libgui.core;

import com.mason.libgui.components.panes.PannablePane;


import static com.mason.libgui.components.buttons.Button.getBlankButton;
import static com.mason.libgui.components.buttons.TrapezoidalButton.getBlankButton;
import static com.mason.libgui.utils.StyleInfo.*;

/**
 *
 * @author Adam Whittaker
 */
public class Launcher{
    
    
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final GUIManager gui = new GUIManager(WIDTH, HEIGHT, "Boo");
    
    
    public static void main(String[] args){
        gui.start();

        /*int LINE_WIDTH = 6;

        Button[] buttons1 = new Button[24], buttons2 = new Button[24], buttons3 = new Button[24];
        for(int n=0; n<buttons1.length; n++){
            buttons1[n] = getBlankButton(ALTERNATE_STYLE_INFO_1, 0, 0, 40, 40);
            buttons2[n] = getBlankButton(ALTERNATE_STYLE_INFO_2, 0, 0, 40, 40);
            buttons3[n] = getBlankButton(DEFAULT_STYLE_INFO, 0, 0, 40, 40);
        }

        InventoryPane inv1 = new InventoryPane(ALTERNATE_STYLE_INFO_1, 64, 2*LINE_WIDTH, buttons1, 6);
        InventoryPane inv2 = new InventoryPane(ALTERNATE_STYLE_INFO_2, 64, 2*LINE_WIDTH, buttons2, 6);
        InventoryPane inv3 = new InventoryPane(DEFAULT_STYLE_INFO, 64, 2*LINE_WIDTH, buttons3, 6);

        Inventory inv = new Inventory(DEFAULT_STYLE_INFO, new InventoryPane[]{inv1, inv2, inv3}, UIAligner.Direction.UP);

        gui.addComponent(inv, START, START);*/

        /*Pane scroll = new ScrollablePane(StyleInfo.DEFAULT_STYLE_INFO, 64, 64, 1024, 1024, 500, 500, false);
        Pane p = new Pane(StyleInfo.DEFAULT_STYLE_INFO, 400, 400, 400, 400);
        p.addComponent(new DraggableComponent(200, 200, 64, 64){

            @Override
            public void render(Graphics2D g){
                g.setColor(Color.PINK);
                g.fillRect(x, y, width, height);
            }

        });
        scroll.addComponent(p);

        gui.addComponent(scroll);*/

        UIComponent comp = //PanLockedDraggableComponent.getTestInstance(100, 100, 100, 100);
                    UIComponent.getTestInstance(250, 250, 100, 100);

        PannablePane pane = new PannablePane(DEFAULT_STYLE_INFO, 100, 100, 600, 600, 300, 300, true);

        pane.addComponent(comp);

        gui.addComponent(pane);

        //gui.addComponent(new Dialogue(50, 50, 270, "Title", "The quick brown fox jumped over the lazy dog.", buttons));
        //gui.addComponent(new DraggablePane(ALTERNATE_STYLE_INFO_1, 250, 200, 200, 200));

        //gui.addComponent(getBlankButton("Slightly longer button", 50, 250));

        //Button b = new TrapezoidalButton(DEFAULT_STYLE_INFO, 30, 130, 100, 32, 24, UIAligner.Direction.DOWN);

        //gui.addComponent(b);

        
        //SmoothSlider s = SmoothSlider.getDefaultSlider(310, 130, 150, false);
        //pane.addComponent(b);
        //pane.addComponent(text);
        //gui.addComponent(pane);
        //gui.addComponent(d);
    }
    
}
