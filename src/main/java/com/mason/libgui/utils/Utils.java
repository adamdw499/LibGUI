
package com.mason.libgui.utils;

import java.awt.Color;
import java.io.FileNotFoundException;

/**
 *
 * @author Adam Whittaker
 */
public final class Utils{
    
    
    private Utils(){}
    
    
    public static PerformanceLog PERFORMANCE_LOG;
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
    
    
    public static void main(String[] args){
        Color[] cols = ColorScheme.analogousColors(new Color(174, 25, 78));
        System.out.println(cols[0]);
        System.out.println(cols[1]);
        System.out.println(cols[2]);
    }
    
}
