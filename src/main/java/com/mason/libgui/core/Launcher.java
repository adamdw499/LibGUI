
package com.mason.libgui.core;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.buttons.TextButton;
import com.mason.libgui.components.buttons.TrapezoidalButton;
import com.mason.libgui.components.dialogues.Dialogue;
import com.mason.libgui.components.inventory.Inventory;
import com.mason.libgui.components.inventory.InventoryPane;
import com.mason.libgui.components.misc.UIText;
import com.mason.libgui.components.misc.UITextInput;
import com.mason.libgui.components.panes.DraggablePane;
import com.mason.libgui.components.panes.Pane;
import com.mason.libgui.components.panes.ScrollablePane;
import com.mason.libgui.components.panes.SlidingPane;
import com.mason.libgui.test.testComponents.Gradient;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner;
import com.mason.libgui.utils.boxPacking.HorizontalBoxPacker;
import com.mason.libgui.utils.boxPacking.VerticalBoxPacker;

import static com.mason.libgui.components.buttons.TextButton.getBlankButton;
import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;
import static com.mason.libgui.utils.StyleInfo.*;
import static com.mason.libgui.utils.UIAligner.Position.*;

/**
 *
 * @author Adam Whittaker
 */
public class Launcher{
    
    
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final GUIManager gui = new GUIManager(WIDTH, HEIGHT, "Boo");
    
    
    public static void main(String[] args){
        gui.start();
        
        //SlidingPane pane = new SlidingPane(120, 16, 400, 300, UIAligner.Direction.RIGHT, 21, WIDTH, HEIGHT);
        
        //Button b = new Toggle(DEFAULT_STYLE_INFO, 50, 50, 64, 64);
        //UIText text = new Tooltip("The text", ALPHA_STYLE_INFO, 150, 150, 75, 32);

        /*Button[] buttons1 = new Button[24], buttons2 = new Button[24], buttons3 = new Button[24];
        for(int n=0; n<buttons1.length; n++){
            buttons1[n] = new Button(ALTERNATE_STYLE_INFO_1, 0, 0, 40, 40);
            buttons2[n] = new Button(ALTERNATE_STYLE_INFO_2, 0, 0, 40, 40);
            buttons3[n] = new Button(DEFAULT_STYLE_INFO, 0, 0, 40, 40);
        }

        InventoryPane inv1 = new InventoryPane(ALTERNATE_STYLE_INFO_1, 64, 2*LINE_WIDTH, buttons1, 6);
        InventoryPane inv2 = new InventoryPane(ALTERNATE_STYLE_INFO_2, 64, 2*LINE_WIDTH, buttons2, 6);
        InventoryPane inv3 = new InventoryPane(DEFAULT_STYLE_INFO, 64, 2*LINE_WIDTH, buttons3, 6);

        Button[] selectors = new Button[]{
                new TrapezoidalButton(ALTERNATE_STYLE_INFO_1, 0, 0, 124, 24, 24, UIAligner.Direction.UP),
                new TrapezoidalButton(ALTERNATE_STYLE_INFO_2, 50, 0, 124, 24, 24, UIAligner.Direction.UP),
                new TrapezoidalButton(DEFAULT_STYLE_INFO, 100, 0, 124, 24, 24, UIAligner.Direction.UP)
        };

        Inventory inv = new Inventory(new InventoryPane[]{inv1, inv2, inv3}, selectors, UIAligner.Direction.UP);

        gui.addComponent(inv, MIDDLE, MIDDLE);*/

        //Pane p = new ScrollablePane(46, 56, 800, 800, 450, 450, false);
        //p.addComponent(new Gradient(0, 0, 1000, 1000));
        //gui.addComponent(p);

        /*Button[] buttons = new Button[]{
                getBlankButton("uwu", -1, -1),
                getBlankButton("Button", -1, -1),
                getBlankButton("Slightly longer button", -1, -1)
        };*/

        gui.addComponent(new UITextInput(100, 100, 200, 50, gui));

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
