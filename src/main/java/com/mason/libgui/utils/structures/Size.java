package com.mason.libgui.utils.structures;

public record Size(int width, int height) implements Boundable{


    public Size(int width, int height){
        this.width = width;
        this.height = height;
        ensureSizePositive();
    }

    private void ensureSizePositive(){
        if(width <= 0 || height <= 0){
            throw new IllegalArgumentException("Trying to construct a Size with non-positive parameters: " + width + ", " + height);
        }
    }


    @Override
    public boolean withinBounds(Coord c){
        return toRect().withinBounds(c);
    }

    public Rect toRect(){
        return new Rect(0, 0, width, height);
    }

}
