package com.mason.libgui.utils.boxPacking;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.exceptions.OversizedComponentException;

public class LeftAlignedBoxPacker implements BoxPacker{


    private final int padding;


    public LeftAlignedBoxPacker(int padding){
        this.padding = padding;
    }

    public LeftAlignedBoxPacker(StyleInfo info){
        this(info.getLineWidth());
    }


    @Override
    public <T extends UIComponent> void pack(int x, int y, int width, int height, T[] components){
        int s = padding + y;

        int currentLineHeight = -1;
        int position = 2*padding + x;
        for(int n=0; n < components.length; n++){
            components[n].setX(position);
            components[n].setY(s);
            if(currentLineHeight < components[n].getHeight()) currentLineHeight = components[n].getHeight();
            if(n < components.length - 1){
                if(position + components[n+1].getWidth() + padding > width - padding){
                    if(position == 2 * padding + x) throw new OversizedComponentException(components[n+1], width, height);
                    position = 2 * padding + x;
                    s += currentLineHeight + padding;
                    currentLineHeight = -1;
                }else{
                    position += components[n].getWidth() + padding;
                }
            }else if(position + components[n].getWidth() + padding > width - padding){
                throw new OversizedComponentException(components[n], width, height);
            }
            if(s + currentLineHeight > height) throw new OversizedComponentException(components[n], width, height);
        }
    }

}
