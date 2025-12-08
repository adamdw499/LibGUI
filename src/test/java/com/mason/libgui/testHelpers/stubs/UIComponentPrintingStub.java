package com.mason.libgui.testHelpers.stubs;

import com.mason.libgui.testHelpers.behaviours.MouseInputEventPrinter;
import com.mason.libgui.utils.structures.*;

import java.awt.*;

public class UIComponentPrintingStub extends DefaultUIComponent{


    private boolean printTicks = false;


    private UIComponentPrintingStub(Coord topLeft, Size size, Color color){
        super(topLeft, size, color, null, MouseInputEventPrinter::new);
        setTicker(this::ticker);
    }

    public static UIComponentPrintingStub buildDefault(Coord topLeft, Size size){
        return build(topLeft, size, new Color(200, 180, 170));
    }

    public static UIComponentPrintingStub build(Coord topLeft, Size size, Color color){
        return new UIComponentPrintingStub(topLeft, size, color);
    }


    public void setPrintTicks(boolean printTicks){
        this.printTicks = printTicks;
    }

    private void ticker(){
        if(printTicks){
            System.out.println(this + " ticked");
        }
    }


}
