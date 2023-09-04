package com.mason.libgui.test.testComponents;

import com.mason.libgui.core.UIComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GridTest extends UIComponent{


    private BufferedImage img;


    public GridTest(int _x, int _y, int w, int h){
        super(_x, _y, w, h);
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = img.getRaster();
        int[] pixel = new int[]{0,0,0};
        for(int x=0;x<img.getWidth();x++) for(int y=0;y<img.getHeight();y++){
            pixel[0] = (x/64 % 2 == 0) ? 180 : 30;
            pixel[1] = (y/64 % 2 == 0) ? 180 : 30;
            pixel[2] = ((x/512 % 2 == 0) || (y/128 % 2 == 0)) && !((x/512 % 2 == 0) && (y/128 % 2 == 0)) ? 190 : 0;
            raster.setPixel(x, y, pixel);
        }
    }


    @Override
    public void render(Graphics2D g){
        g.drawImage(img, null, x, y);
    }

    @Override
    public void tick(int mx, int my){}

}
