package com.mason.libgui.utils.boxPacking;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.exceptions.OversizedComponentException;

import java.util.Arrays;
import java.util.LinkedList;

public class CenterAlignedBoxPacker implements BoxPacker{


    private final int padding;


    public CenterAlignedBoxPacker(int padding){
        this.padding = padding;
    }

    public CenterAlignedBoxPacker(StyleInfo info){
        this(info.RENDER_UTILS.getLineWidth());
    }


    @Override
    public <T extends UIComponent> void pack(int x, int y, int width, int height, T[] components){
        int s = padding + y;

        for(LinkedList<T> line : partitionComponents(width, height, components)){
            positionLine(x, s, width, line);
            s += getLineHeight(line) + padding;
        }
    }

    private <T extends UIComponent> LinkedList<LinkedList<T>> partitionComponents(int width, int height, T[] components){
        LinkedList<LinkedList<T>> partition = new LinkedList<>();
        LinkedList<T> currentLine = new LinkedList<>();
        int w = padding;

        for(T component : components){
            w += component.getWidth() + padding;
            if(w > width){
                partition.add(currentLine);
                currentLine = new LinkedList<>();
                currentLine.add(component);
                w = padding + component.getWidth();
            }else{
                currentLine.add(component);
            }
        }
        if(!currentLine.isEmpty()) partition.add(currentLine);

        return partition;
    }

    private <T extends UIComponent> int getLineHeight(LinkedList<T> line){
        return line.stream().mapToInt(UIComponent::getHeight).max().orElse(0);
    }

    private <T extends UIComponent> int sumPaddedWidth(LinkedList<T> line){
        return padding + line.stream().mapToInt(comp -> comp.getWidth() + padding).sum();
    }

    private <T extends UIComponent> void positionLine(int x, int s, int width, LinkedList<T> line){
        int pos = x + (width - sumPaddedWidth(line))/2 + padding;
        for(T comp : line){
            comp.setX(pos);
            comp.setY(s);
            pos += padding + comp.getWidth();
        }
    }

}
