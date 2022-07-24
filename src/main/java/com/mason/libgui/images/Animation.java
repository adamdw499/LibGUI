package com.mason.libgui.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Animation{


    private BufferedImage[] frames;
    private final int ticksPerFrame;
    private int ticksPassed = 0;
    private int currentFrame;
    private boolean repeat;



    public Animation(SpriteSheet sp, String key, int ticksPerFrame, boolean repeat){
        int n = 0;
        LinkedList<BufferedImage> lst = new LinkedList<>();
        while(sp.containsKey(key + "-" + n)){
            lst.add(sp.get(key + "-" + n));
            n++;
        }
        frames = lst.toArray(new BufferedImage[n]);
        this.ticksPerFrame = ticksPerFrame;
        this.repeat = repeat;
    }

    public Animation(SpriteSheet sp, String key, boolean repeat){
        this(sp, key, 1, repeat);
    }


    public void render(Graphics2D g, int x, int y){
        g.drawImage(frames[currentFrame], null, x, y);
    }

    public void tick(){
        ticksPassed++;
        if(ticksPassed >= ticksPerFrame){
            ticksPassed = 0;
            if(currentFrame < frames.length-1){
                currentFrame++;
            }else if(repeat){
                currentFrame = 0;
            }
        }
    }

}
