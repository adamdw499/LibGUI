package com.mason.libgui.utils.boxPacking;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.exceptions.OversizedComponentException;

import java.util.Arrays;

public class VerticalBoxPacker implements BoxPacker{


    private final int padding;


    public VerticalBoxPacker(int padding){
        this.padding = padding;
    }

    public VerticalBoxPacker(StyleInfo info){
        this(info.getLineWidth());
    }


    @Override
    public <T extends UIComponent> void pack(int x, int y, int width, int height, T[] components){
        check(components, width, height);
        int pos = y + (height - sumPaddedHeight(components))/2 + padding;
        for(T comp : components){
            comp.setY(pos);
            comp.setX(x + width/2 - comp.getWidth()/2);
            pos += padding + comp.getHeight();
        }
    }


    protected <T extends UIComponent> void check(T[] components, int width, int height){
        if(maxWidth(components) > width || sumPaddedHeight(components) > height)
            throw new OversizedComponentException(width, height);
    }

    private <T extends UIComponent> int maxWidth(T[] components){
        return 2*padding + Arrays.stream(components).mapToInt(UIComponent::getWidth).max().orElse(0);
    }

    private <T extends UIComponent> int sumPaddedHeight(T[] components){
        return padding + Arrays.stream(components).mapToInt(comp -> comp.getHeight() + padding).sum();
    }

}
