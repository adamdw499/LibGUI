package com.mason.libgui.utils.boxPacking;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.exceptions.OversizedComponentException;

import java.util.Arrays;

public class HorizontalBoxPacker implements BoxPacker{


    private final int padding;


    public HorizontalBoxPacker(int padding){
        this.padding = padding;
    }

    public HorizontalBoxPacker(StyleInfo info){
        this(info.RENDER_UTILS.getLineWidth());
    }


    @Override
    public <T extends UIComponent> void pack(int x, int y, int width, int height, T[] components){
        check(components, width, height);
        int pos = x + (width - sumPaddedWidth(components))/2 + padding;
        for(T comp : components){
            comp.setX(pos);
            comp.setY(y + height/2 - comp.getHeight()/2);
            pos += padding + comp.getWidth();
        }
    }


    protected <T extends UIComponent> void check(T[] components, int width, int height){
        if(maxHeight(components) > height || sumPaddedWidth(components) > width)
            throw new OversizedComponentException(width, height);
    }

    private <T extends UIComponent> int maxHeight(T[] components){
        return 2*padding + Arrays.stream(components).mapToInt(UIComponent::getHeight).max().orElse(0);
    }

    private <T extends UIComponent> int sumPaddedWidth(T[] components){
        return padding + Arrays.stream(components).mapToInt(comp -> comp.getWidth() + padding).sum();
    }

}
