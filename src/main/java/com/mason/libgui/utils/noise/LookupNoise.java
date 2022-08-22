package com.mason.libgui.utils.noise;

public class LookupNoise extends PerlinNoise{


    /**
     * Creates a new instance.
     *
     * @param width  The width of the target 2D double array.
     * @param height The height of the target 2D double array.
     * @param amp    The approximate amplitude of the oscillations.
     * @param oc     The number of iterations.
     * @param l      The lacunarity.
     * @param p      The persistence.
     */
    public LookupNoise(int width, int height, double amp, int oc, double l, double p){
        super(width, height, amp, oc, l, p);
    }


    public void apply(double[][] map){
        double[][] initial = new double[map.length][map[0].length];
        super.apply(initial);
        for(int y=0; y < map.length;y++){
            for(int x=0; x < map[y].length;x++){
                map[y][x] = initial[(int)(x+map.length*map[y][x])%map.length][y];
            }
        }
    }


}
