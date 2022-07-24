package com.mason.libgui.images;

import com.mason.libgui.utils.ExceptionHandler;
import com.mason.libgui.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheet extends HashMap<String, BufferedImage>{


    public SpriteSheet(String filepath, String[] keys, int pixelLength, ExceptionHandler ex){
        BufferedImage spriteSheet = Utils.loadImage(filepath, ex);
        BufferedImage img = new BufferedImage(pixelLength, pixelLength, BufferedImage.TYPE_INT_ARGB);
        int n = 0;
        for(int y=0; y < spriteSheet.getHeight(); y += pixelLength){
            for(int x=0; x < spriteSheet.getWidth(); x += pixelLength){
                if(keys[n].equals("NULL")) this.put(keys[n], spriteSheet.getSubimage(x, y, pixelLength, pixelLength));
                n++;
            }
        }
    }

    public SpriteSheet(){}

    public SpriteSheet(Map<String, BufferedImage> map){
        super(map);
    }

}
