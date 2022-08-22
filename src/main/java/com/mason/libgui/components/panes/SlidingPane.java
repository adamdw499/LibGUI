package com.mason.libgui.components.panes;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Direction;

import java.awt.event.MouseEvent;

public class SlidingPane extends Pane{


    private MotionManager motion;


    public SlidingPane(StyleInfo info, int x, int y, int w, int h, Direction direction, double speed, int super_width, int super_height){
        super(info, x, y, w, h);
        int retractedCoord;
        switch(direction) {
            case LEFT -> retractedCoord = info.getLineWidth() - width;
            case RIGHT -> retractedCoord = super_width - info.getLineWidth();
            case UP -> retractedCoord = info.getLineWidth() - height;
            case DOWN -> retractedCoord = super_height - info.getLineWidth();
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        motion = new MotionManager(direction, speed, x, y, w, h, retractedCoord);
    }

    public SlidingPane(StyleInfo info, int x, int y, int w, int h, Direction direction, double speed){
        this(info, x, y, w, h, direction, speed, w, h);
    }


    @Override
    public void tick(int mx, int my){
        if(motion.isMoving()){
            motion.tick();
            x = motion.getX();
            y = motion.getY();
        }
        super.tick(mx, my);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(motion.withinBounds(e.getX(), e.getY())) motion.mouseClicked(e);
        else super.mouseClicked(e);
    }


    protected class MotionManager extends UIComponent{


        Direction direction;
        double speed;
        boolean retracted = false, moving = false;
        int retractedCoord, extendedCoord;
        double delta = 0;


        public MotionManager(Direction dir, double sp, int x, int y, int w, int h, int retractedCoord){
            super(x, y, w, h);
            direction = dir;
            speed = sp;
            this.retractedCoord = retractedCoord;
            switch(direction){
                case UP, DOWN -> extendedCoord = y;
                case LEFT, RIGHT -> extendedCoord = x;
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            }
        }


        public void tick(){
            switch(direction){
                case LEFT -> {
                    if(retracted) moveRight();
                    else moveLeft();
                }
                case RIGHT -> {
                    if(retracted) moveLeft();
                    else moveRight();
                }
                case UP -> {
                    if(retracted) moveDown();
                    else moveUp();
                }
                case DOWN -> {
                    if(retracted) moveUp();
                    else moveDown();
                }
            }
        }

        public void moveLeft(){
            delta += speed;
            while(delta >= 1){
                x -= 1;
                delta -= 1;
            }
            if(retracted && x<=extendedCoord){
                x = extendedCoord;
                retracted = false;
                moving = false;
            }else if(!retracted && x<=retractedCoord){
                x = retractedCoord;
                retracted = true;
                moving = false;
            }
        }

        public void moveRight(){
            delta += speed;
            while(delta >= 1){
                x += 1;
                delta -= 1;
            }
            if(retracted && x>=extendedCoord){
                x = extendedCoord;
                retracted = false;
                moving = false;
            }else if(!retracted && x>=retractedCoord){
                x = retractedCoord;
                retracted = true;
                moving = false;
            }
        }

        public void moveUp(){
            delta += speed;
            while(delta >= 1){
                y -= 1;
                delta -= 1;
            }
            if(retracted && y<=extendedCoord){
                y = extendedCoord;
                retracted = false;
                moving = false;
            }else if(!retracted && y<=retractedCoord){
                y = retractedCoord;
                retracted = true;
                moving = false;
            }
        }

        public void moveDown(){
            delta += speed;
            while(delta >= 1){
                y += 1;
                delta -= 1;
            }
            if(retracted && y>=extendedCoord){
                y = extendedCoord;
                retracted = false;
                moving = false;
            }else if(!retracted && y>=retractedCoord){
                y = retractedCoord;
                retracted = true;
                moving = false;
            }
        }


        public boolean isMoving(){
            return moving;
        }


        @Override
        public boolean withinBounds(int mx, int my){
            return (x<mx && mx<x+info.getLineWidth() && y<my && my<y+height) ||
                    (mx<width+x && x+width-info.getLineWidth()<mx && y<my && my<y+height) ||
                    (x<mx && mx<x+width && y<my && my<y+info.getLineWidth()) ||
                    (x<mx && mx<x+width && y+height-info.getLineWidth()<my && my<y+height);
        }

        @Override
        public void mouseClicked(MouseEvent e){
            if(!moving) moving = true;
        }

    }

}
