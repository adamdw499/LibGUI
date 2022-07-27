
package com.mason.libgui.utils;

import com.mason.libgui.core.GUIManager;
import com.mason.libgui.utils.exceptionHandlers.FailExceptionHandler;
import com.mason.libgui.utils.exceptionHandlers.FreezeExceptionHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Adam Whittaker
 */
public final class Utils{
    
    
    private Utils(){}
    
    
    public static PerformanceLog PERFORMANCE_LOG;
    public static ExceptionHandler DEFAULT_EXCEPTION_HANDLER = new FreezeExceptionHandler();
    static{
        try{
            PERFORMANCE_LOG = new PerformanceLog();
        }catch(FileNotFoundException e){
            System.err.println("PrintStream failed.");
        }
    }
    
    
    /**
     * A marker annotation for unfinished methods/classes.
     */
    public static @interface Unfinished{
        String value() default "";
    }
    
    /**
     * An annotation to keep track of when each method has been tested.
     */
    public static @interface Tested{
        String value() default ""; //Store the date of current testing.
    }
    
    
    public static void printArray(float[] ary){
        System.out.print("[");
        for(int n=0;n<ary.length-1;n++){
            System.out.print(ary[n] + ", ");
        }
        System.out.println(ary[ary.length-1] + "]");
    }


    public static BufferedImage loadImage(String filepath, ExceptionHandler ex){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(filepath));
        }catch(Exception e){
            ex.handleException(e);
        }
        return img;
    }

    public static BufferedImage loadImage(String filepath, GUIManager gui){
        return loadImage(filepath, gui.getExceptionHandler());
    }

    public static boolean isAlphanumeric(char c){
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ' || c == '.' || c == '-';
    }


    public static int[] stringDimension(String text, Font font){
        AffineTransform transform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(transform, true, true);
        return new int[]{
                (int)(font.getStringBounds(text, frc).getWidth()),
                (int)(font.getStringBounds(text, frc).getHeight())
        };
    }

    
    public static void main(String[] args){
        RenderUtils.drawBorder(null, null, new Polygon(new int[]{50, 100, 100, 50}, new int[]{170, 50, 170, 50}, 4));
    }
    
}
