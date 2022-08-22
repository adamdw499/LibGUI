package com.mason.libgui.utils.noise;

import static com.mason.libgui.utils.Utils.R;

/**
 * The midpoint displacement noise algorithm. (See Documentation in project
 * pseudo-code)
 * @author Adam Whittaker
 */
public class MidpointDisplacementNoise extends Noise{


    /**
     * initialHeight: The initial altitude of the height map.
     * initialJitter: The starting random displacement factor.
     * jitterDecay: The factor by which the random displacement factor decays
     * each iteration.
     * maxHeight: The maximum altitude.
     * tileArtefacts: Whether the noise will retain the square-based structure
     * of the generation algorithm (tiles look squarish and more regular).
     */
    private final double initialJitter, jitterDecay;
    private final boolean tileArtefacts;


    /**
     * Creates a new instance.
     * @param iJ The starting random displacement factor.
     * @param jD The factor by which the random displacement factor decays
     * each iteration.
     * @param tA Whether the noise will retain the square-based structure
     * of the generation algorithm (tiles look squarish and more regular).
     */
    public MidpointDisplacementNoise(double iJ, double jD, boolean tA){
        initialJitter = iJ;
        jitterDecay = jD;
        tileArtefacts = tA;
    }


    /**
     * Generates a randomized height map on the given 2D double array.
     */
    public void apply(double[][] map){
        //Initializes the corner pixels randomly.
        map[0][0] = 2D*R.nextDouble() - 1D;
        map[0][map[0].length - 1] = 2D*R.nextDouble() - 1D;
        map[map.length - 1][0] = 2D*R.nextDouble() - 1D;
        map[map.length - 1][map[0].length - 1] = 2D*R.nextDouble() - 1D;
        //Performs the algorithm recursively
        fillRectangle(map, 0, 0, map[0].length - 1, map.length - 1, initialJitter);
    }

    /**
     * Recursively performs the algorithm on the given rectangular section of
     * the given double map.
     * @param map The 2D double array.
     * @param tlx The x coordinate of the top left corner of the rectangle.
     * @param tly The y coordinate of the top left corner of the rectangle.
     * @param brx The x coordinate of the bottom right corner of the rectangle.
     * @param bry The y coordinate of the bottom right corner of the rectangle.
     * @param jitter The current random amplitude displacement factor.
     */
    private void fillRectangle(double[][] map, int tlx, int tly, int brx, int bry, double jitter){
        //Finds the centre of the map.
        int xA = (tlx + brx) / 2, yA = (tly + bry) / 2;

        //Computes the midpoints of the edges.
        if(map[tly][xA] == 0)
            map[tly][xA] = (map[tly][tlx] + map[tly][brx]) / 2D + R.nextDouble() * jitter - jitter / 2;
        if(map[bry][xA] == 0)
            map[bry][xA] = (map[bry][tlx] + map[bry][brx]) / 2D + R.nextDouble() * jitter - jitter / 2;
        if(map[yA][tlx] == 0)
            map[yA][tlx] = (map[tly][tlx] + map[bry][tlx]) / 2D + R.nextDouble() * jitter - jitter / 2;
        if(map[yA][brx] == 0)
            map[yA][brx] = (map[tly][brx] + map[bry][brx]) / 2D + R.nextDouble() * jitter - jitter / 2;

        //Resets them to be within the range.
        if(map[tly][xA] > 1) map[tly][xA] = 1;
        if(map[bry][xA] > 1) map[bry][xA] = 1;
        if(map[yA][tlx] > 1) map[yA][tlx] = 1;
        if(map[yA][brx] > 1) map[yA][brx] = 1;

        if(map[tly][xA] < -1) map[tly][xA] = -1;
        if(map[bry][xA] < -1) map[bry][xA] = -1;
        if(map[yA][tlx] < -1) map[yA][tlx] = -1;
        if(map[yA][brx] < -1) map[yA][brx] = -1;

        //If the midpoint is the same as the top left, this branch of the
        //recursive algorithm is done.
        if(xA == tlx || yA == tly) return;

        //Calculates the central pixel.
        map[yA][xA] = (map[tly][tlx] + map[tly][brx]
                + map[bry][tlx] + map[bry][brx]) / 4D + R.nextDouble() * jitter;
        if(!tileArtefacts) map[yA][xA] -= jitter / 2;

        //Ensures it to within the range.
        if(map[yA][xA] > 1) map[yA][xA] = 1;
        else if(map[yA][xA] < -1) map[yA][xA] = -1;

        //Recursively performs the algorithm on the four sub-rectangles.
        fillRectangle(map, xA, yA, brx, bry, jitter * jitterDecay);
        fillRectangle(map, tlx, yA, xA, bry, jitter * jitterDecay);
        fillRectangle(map, xA, tly, brx, yA, jitter * jitterDecay);
        fillRectangle(map, tlx, tly, xA, yA, jitter * jitterDecay);
    }

}
