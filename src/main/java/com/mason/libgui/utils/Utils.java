
package com.mason.libgui.utils;

import com.mason.libgui.core.GUIManager;
import com.mason.libgui.images.SpriteSheet;
import com.mason.libgui.utils.exceptionHandlers.FailExceptionHandler;
import com.mason.libgui.utils.exceptionHandlers.FreezeExceptionHandler;
import com.mason.libgui.utils.noise.MidpointDisplacementNoise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import static java.lang.Math.exp;

/**
 *
 * @author Adam Whittaker
 */
public final class Utils{
    
    
    private Utils(){}
    
    
    public static PerformanceLog PERFORMANCE_LOG;
    public static SpriteSheet SPRITE_SHEET;
    public static final Random R = new Random();
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
    public @interface Unfinished{
        String value() default "";
    }
    
    /**
     * An annotation to keep track of when each method has been tested.
     */
    public @interface Tested{
        String value() default ""; //Store the date of current testing.
    }
    
    
    public static void printArray(float[] ary){
        System.out.print("[");
        for(int n=0;n<ary.length-1;n++){
            System.out.print(ary[n] + ", ");
        }
        System.out.println(ary[ary.length-1] + "]");
    }


    public static BufferedImage readImage(String filepath, ExceptionHandler ex){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(filepath));
        }catch(Exception e){
            ex.handleException(e);
        }
        return img;
    }

    public static BufferedImage readImage(String filepath, GUIManager gui){
        return readImage(filepath, gui.getExceptionHandler());
    }

    public static void setSpriteSheet(String prefix, String[] filepaths, String suffix){
        SPRITE_SHEET = new SpriteSheet(prefix, filepaths, suffix, DEFAULT_EXCEPTION_HANDLER);
    }

    public static void setSpriteSheet(SpriteSheet sheet){
        SPRITE_SHEET = sheet;
    }

    public static BufferedImage loadSprite(String key){
        BufferedImage img = SPRITE_SHEET.get(key);
        if(img == null) img = Utils.loadSprite("ICON_NOT_FOUND");
        return img;
    }

    public static void writeImage(String formatName, String filepath, BufferedImage img, ExceptionHandler ex){
        try{
            ImageIO.write(img, formatName, new File(filepath));
        }catch(Exception e){
            ex.handleException(e);
        }
    }

    public static void setRandomSeed(long seed){
        R.setSeed(seed);
    }

    /**
     * Hermit's smoothing cumulative distribution function.
     * @param x A number between 0 and 1
     * @return A smoother version of the number (closer to 0.5).
     */
    public static double fade(double x){
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    /**
     * Interpolates the value between the two given points based on the given
     * weight.
     * @param a The first value.
     * @param b The second value.
     * @param x The relative weight of the second value (0: a, 1: b).
     */
    public static double interpolate(double a, double b, double x){
        return (1D - x) * a + x * b;
    }

    public static double sigmoid(double x){
        return 1D/(1D+exp(-x));
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

    public static int calcFontHeight(Font font){
        return stringDimension("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm .-", font)[1];
    }

    public static int calcAverageCharWidth(Font font){
        String str = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm .-";
        return stringDimension(str, font)[0]/str.length();
    }

    
    public static void main(String[] args){
        MidpointDisplacementNoise mdp = new MidpointDisplacementNoise(1, 0.75D, false);
        double[][] map = new double[128][128];
        mdp.apply(map);
        BufferedImage img = ImageUtils.getDichromeTexture(128, 128, 0, 0,
                new Color(120, 30, 50),
                new Color(30, 100, 90), map);
        Graphics2D g = img.createGraphics();
        g.drawImage(readImage("testImages/filter3.png", new FailExceptionHandler()), null,0, 0);
        writeImage("png", "testImages/mdn.png", img, new FailExceptionHandler());
        /*BufferedImage img = loadImage("testImages/filter3.png", new FailExceptionHandler());
        ImageUtils.applyFadeFactor(img, 0.5);
        writeImage("png", "testImages/filter4.png", img, new FailExceptionHandler());
        ImageUtils.applyFadeFactor(img, 0.5);
        writeImage("png", "testImages/filter5.png", img, new FailExceptionHandler());
        ImageUtils.applyFadeFactor(img, 0.5);
        writeImage("png", "testImages/filter6.png", img, new FailExceptionHandler());*/
    }
    
}
