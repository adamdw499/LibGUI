package com.mason.libgui.utils.noise;

import static com.mason.libgui.utils.Utils.*;

/**
 * Generates wrappable noise using the Perlin noise algorithm.
 * @author Adam Whittaker
 */
public class PerlinNoise extends Noise{


    /**
     * vec: The grid of random gradient vectors.
     * octaveNum: The number of successive iterations of the algorithm.
     * lacunarity: How much the "size" of the noise will be retained in
     * successive octaves.
     * persistence: How much of the amplitude of the noise will be retained in
     * successive octaves.
     * amplitude: The amplitude of the noise.
     */
    private final Vector[][] vec;
    private final int octaveNum;
    private final double lacunarity, persistence;
    private double amplitude;


    /**
     * Creates a new instance.
     * @param width The width of the target 2D double array.
     * @param height The height of the target 2D double array.
     * @param amp The approximate amplitude of the oscillations.
     * @param oc The number of iterations.
     * @param l The lacunarity.
     * @param p The persistence.
     */
    public PerlinNoise(int width, int height, double amp, int oc, double l, double p){
        vec = new Vector[height][width];
        octaveNum = oc;
        lacunarity = l;
        persistence = p;
        amplitude = amp;
        initializeRandomVectors();
    }


    /**
     * Iterates through the internal grid of vectors and initializes them with a
     * random direction and unit length.
     */
    private void initializeRandomVectors(){
        for(int y = 0; y < vec.length; y++){
            for(int x = 0; x < vec[0].length; x++){
                vec[y][x] = new Vector();
            }
        }
    }

    /**
     * Generates noise on the given 2D double array using the Perlin noise
     * algorithm.
     */
    @Override
    public void apply(double[][] map){
        //The initial grid size is a quarter of the array.
        int xCoarse = map[0].length/4, yCoarse = map.length/4;
        for(int n = 0; n < octaveNum; n++){
            //Adds an octave of noise to the array.
            overlayOctave(map, amplitude, xCoarse, yCoarse);
            //Decreases the amplitude and grid size for the next iteration.
            amplitude *= persistence;
            xCoarse *= lacunarity;
            yCoarse *= lacunarity;
            if(xCoarse<1) xCoarse = 1;
            if(yCoarse<1) yCoarse = 1;
        }
    }

    /**
     * Shifts this noise to the range [0,1].
     * @param map The noise map.
     */
    public void normalise(double[][] map){
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE, mean = 0;
        for(double[] vals : map){
            for(double val : vals){
                if(max < val) max = val;
                if(min > val) min = val;
                mean += val;
            }
        }
        mean /= (map.length*map[0].length);
        for(int y=0;y<map.length;y++){
            for(int x=0;x<map[0].length;x++){
                map[y][x] = (map[y][x]-mean-min)/(max-min);
            }
        }
    }

    /**
     * Adds one iteration of the algorithm onto the given map.
     * @param map The map.
     * @param p The persistence.
     * @param xCoarse The width of the current Perlin rectangle.
     * @param yCoarse The height of the current Perlin rectangle.
     */
    private void overlayOctave(double[][] map, double p, int xCoarse, int yCoarse){
        for(int y = 0; y < map.length; y++){
            for(int x = 0; x < map[y].length; x++){
                //Calculates the perlin noise function for one point in the map.
                map[y][x] += p * getPerlin(x, y, xCoarse, yCoarse);
            }
        }
    }

    /**
     * Finds the height of a pixel using the algorithm.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double getPerlin(int x, int y, int xC, int yC){
        //Smooths the distance of the point from the side of the grid.
        double xN = fade((double) (x % xC) / xC),
                yN = fade((double) (y % yC) / yC),
                //Dots the vector on the corners of the grid with the current
                //point vector relative to the grid. Interpolates the four
                //results.
                iU = interpolate(dotTL(x, y, xC, yC), dotTR(x, y, xC, yC), xN),
                iD = interpolate(dotBL(x, y, xC, yC), dotBR(x, y, xC, yC), xN);
        return interpolate(iU, iD, yN);
    }

    /**
     * Computes the dot product of the vector at the given coordinates with the
     * displacement of the given point to that vector.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param xV The x coordinate of the vector (in the vector map).
     * @param yV The y coordinate of the vector (in the vector map).
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double dot(double x, double y, int xV, int yV, double xC, double yC){
        int j = yV % vec.length;
        int i = xV % vec[0].length;
        return vec[j][i].v[0] * (x - xV) / xC + vec[j][i].v[1] * (y - yV) / yC;
    }

    /**
     * Computes the dot product for the point and the vector in the top left
     * corner of its Perlin rectangle.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double dotTL(int x, int y, int xC, int yC){
        int xV = x - (x % xC);
        int yV = y - (y % yC);
        return dot(x, y, xV, yV, xC, yC);
    }

    /**
     * Computes the dot product for the point and the vector in the top right
     * corner of its Perlin rectangle.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double dotTR(int x, int y, int xC, int yC){
        int xV = x - (x % xC) + xC;
        int yV = y - (y % yC);
        return dot(x, y, xV, yV, xC, yC);
    }

    /**
     * Computes the dot product for the point and the vector in the bottom left
     * corner of its Perlin rectangle.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double dotBL(int x, int y, int xC, int yC){
        int xV = x - (x % xC);
        int yV = y - (y % yC) + yC;
        return dot(x, y, xV, yV, xC, yC);
    }

    /**
     * Computes the dot product for the point and the vector in the bottom right
     * corner of its Perlin rectangle.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param xC The width of the current Perlin rectangle.
     * @param yC The height of the current Perlin rectangle.
     * @return
     */
    private double dotBR(int x, int y, int xC, int yC){
        int xV = x - (x % xC) + xC;
        int yV = y - (y % yC) + yC;
        return dot(x, y, xV, yV, xC, yC);
    }


    private static class Vector{


        /**
         * v: The values of the vector in double format.
         */
        public final double[] v;


        /**
         * Creates a randomized new instance with unit magnitude.
         */
        public Vector(){
            v = new double[]{R.nextDouble()*2-1D, R.nextDouble()*2-1D};
            double len = Math.sqrt(v[0]*v[0] + v[1]*v[1]);
            v[0] /= len;
            v[1] /= len;
        }

    }

}
