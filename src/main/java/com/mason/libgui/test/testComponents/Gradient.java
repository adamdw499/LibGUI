package com.mason.libgui.test.testComponents;

import com.mason.libgui.core.UIComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Gradient extends UIComponent{


    private BufferedImage img;


    public Gradient(int _x, int _y, int w, int h){
        super(_x, _y, w, h);
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = img.getRaster();
        int[] pixel = new int[]{0,0,0};
        for(int x=0;x<img.getWidth();x++) for(int y=0;y<img.getHeight();y++){
            pixel[0] = (int)( ((double)x)/img.getWidth() * 150D);
            pixel[1] = (int)( ((double)(img.getHeight() + img.getWidth() - x - y))/(img.getWidth() + img.getHeight()) * 150D);
            pixel[2] = (int)( ((double)y)/img.getHeight() * 150D);
            raster.setPixel(x, y, pixel);
        }
    }


    @Override
    public void render(Graphics2D g){
        g.drawImage(img, null, x, y);
    }

}
