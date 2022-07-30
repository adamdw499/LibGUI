package com.mason.libgui.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import static com.mason.libgui.utils.Utils.R;
import static java.lang.Math.min;

public final class ImageUtils{


    private ImageUtils(){}


    /**
     * Applies a random noise bitmap to the alpha channel of the given image.
     * @param img The image.
     * @param midPoint The average noise (0-255).
     * @param jitter The maximum displacement in either direction.
     */
    public static void applyAlphaNoise(BufferedImage img, int midPoint, int jitter){
        WritableRaster raster = img.getAlphaRaster();
        int[] pixel = new int[4];
        for(int y=0;y<img.getHeight();y++){
            for(int x=0;x<img.getWidth();x++){
                pixel = raster.getPixel(x, y, pixel);
                if(pixel[0]==0){
                    pixel[0] = midPoint + R.nextInt(2*jitter) - jitter;
                }else{
                    pixel[0] += R.nextInt(2*jitter) - jitter;
                }
                pixel[0] = min(pixel[0], 255);
                pixel[0] = Math.max(pixel[0], 0);
                raster.setPixel(x, y, pixel);
            }
        }
    }


    /**
     * Gets the color with the given name.
     * @param name The name.
     */
    public static Color getColor(String name){
        return switch(name.toLowerCase()){
            //General colors
            case "apple green" -> Color.decode("#00ff1d");
            case "aquamarine" -> Color.decode("#7FFFD4");
            case "apricot" -> Color.decode("#FBCEB1");
            case "lime" -> Color.decode("#BFFF00");
            case "sky blue" -> Color.decode("#87CEEB");
            case "amber" -> Color.decode("#FFBF00");
            case "auburn" -> Color.decode("#A52A2A");
            case "gold" -> Color.decode("#FFD700");
            case "electrum" -> Color.decode("#fef3d4");
            case "silver" -> Color.decode("#C0C0C0");
            case "azure" -> Color.decode("#007FFF");
            case "magnolia" -> Color.decode("#F4E9D8");
            case "banana" -> Color.decode("#e7f26c");
            case "orange" -> Color.decode("#FF8000");
            case "blizzard blue" -> Color.decode("#50BFE6");
            case "blueberry" -> Color.decode("#4570E6");
            case "cerulean" -> Color.decode("#1DACD6");
            case "periwinckle" -> Color.decode("#C3CDE6");
            case "turquoise" -> Color.decode("#6CDAE7");
            case "rose" -> Color.decode("#ED0A3F");
            case "bubblegum" -> Color.decode("#FC80A5");
            case "burgundy" -> Color.decode("#900020");
            case "chocolate" -> Color.decode("#AF593E");
            case "coral" -> Color.decode("#FF7F50");
            case "cyan" -> Color.decode("#00FFFF");
            case "dandelion" -> Color.decode("#FED85D");
            case "chestnut" -> Color.decode("#954535");
            case "tangerine" -> Color.decode("#FF9966");
            case "lemon" -> Color.decode("#FFFF9F");
            case "ruby" -> Color.decode("#AA4069");
            case "emerald" -> Color.decode("#14A989");
            case "forest green" -> Color.decode("#5FA777");
            case "ginger" -> Color.decode("#f78614");
            case "tea" -> Color.decode("#995006");
            case "voilet" -> Color.decode("#8359A3");
            case "amaranth red" -> Color.decode("#E52B50");
            case "scorpion brown" -> Color.decode("#513315");
            case "amethyst" -> Color.decode("#9966CC");
            case "charcoal" -> Color.decode("#36454F");
            case "asparagus" -> Color.decode("#87A96B");
            case "ash" -> Color.decode("#919191");
            case "copper" -> Color.decode("#B87333");
            case "tin" -> Color.decode("#b5a14a");
            case "beige" -> Color.decode("#F5F5DC");
            case "bistre" -> Color.decode("#3D2B1F");
            case "olive" -> Color.decode("#808000");
            case "bronze" -> Color.decode("#CD7F32");
            case "sapphire" -> Color.decode("#0F52BA");
            case "purple" -> Color.decode("#800080");
            case "boysenberry" -> Color.decode("#910a0a");
            case "ochre" -> Color.decode("#CC7722");
            case "maroon" -> Color.decode("#800000");
            case "lavender" -> Color.decode("#E6E6FA");
            case "lilac" -> Color.decode("#C8A2C8");
            case "sugar brown" -> Color.decode("#aa5500");
            case "coffee" -> Color.decode("#6F4E37");
            case "scarlet" -> Color.decode("#FF2400");
            case "crimson" -> Color.decode("#FF003F");
            case "salmon" -> Color.decode("#FA8072");
            case "metallic" -> Color.decode("#bbbbbb");
            case "mint" -> Color.decode("#3EB489");
            case "saffron" -> Color.decode("#F4C430");
            case "eggplant" -> Color.decode("#614051");
            case "firebrick" -> Color.decode("#ff5400");
            case "flame" -> Color.decode("#f84400");
            case "white wine" -> Color.decode("#dae8a9");
            case "red" -> Color.decode("#ff0000");
            //fantasy materials
            case "red mogle wood" -> Color.decode("#872f10");
            case "hurian titan wood" -> Color.decode("#eddbd5");
            case "hurian goddess wood" -> Color.decode("#edd5e3");
            case "pinkheart wood" -> Color.decode("#edb2af");
            case "spireling wood" -> Color.decode("#602d00");
            case "spickle wood" -> Color.decode("#603a19");
            case "master mogle wood" -> Color.decode("#9e734d");
            case "schmetterhaus wood" -> Color.decode("#639b41");
            case "pingle wood" -> Color.decode("#9b7741");
            case "pongle wood" -> Color.decode("#6d593a");
            case "callop wood" -> Color.decode("#a2adaa");
            case "pesous wood" -> Color.decode("#490b40");
            case "shraub wood" -> Color.decode("#110c00");
            case "hulous wood" -> Color.decode("#7a4d09");
            case "albino mori wood" -> Color.decode("#ede4aa");
            case "thickbranch wood" -> Color.decode("#6b4a00");
            case "roachwood" -> Color.decode("#556b00");
            case "crying brown magmatic wood" -> Color.decode("#5b0606");
            case "white magmatic wood" -> Color.decode("#ffffff");
            case "fungal wood" -> Color.decode("#7bb661").darker().darker();
            case "hellstone" -> Color.decode("#b32134");
            case "downstone" -> Color.decode("#2b2028");
            //real materials
            case "clay" -> Color.decode("#966432");
            case "stone" -> Color.decode("#7b7a73");
            case "marble" -> Color.decode("#8b8563");
            case "birch" -> Color.decode("#e1c785").darker();
            case "dark oak" -> Color.decode("#261609");
            case "redwood" -> Color.decode("#a45a52");
            case "oak" -> Color.decode("#c89959").darker();
            case "mahogany" -> Color.decode("#5a2e11");
            case "spruce" -> Color.decode("#6a432d");
            case "palm" -> Color.decode("#deb887");
            case "ebony" -> new Color(40, 20, 20);
            case "brick" -> Color.decode("#68250a").darker();
            default -> throw new IllegalStateException("Illegal color: " + name);
        };
    }


    public static BufferedImage getDichromeTexture(int width, int height, int xOffset,
                                             int yOffset, Color col1, Color col2, double[][] map){
        int[] pixel = new int[]{0, 0, 0, 255};
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = img.getRaster();
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                getColorAverage(pixel, col1, col2, map[y + yOffset][x + xOffset]);
                raster.setPixel(x, y, pixel);
            }
        }
        return img;
    }


    /**
     * Averages two colors based on a given weight (RGB only).
     * @param pixel The int array for the color to be stored in.
     * @param c1 The first color.
     * @param c2 The second color.
     * @param weight The relative weight of the colors
     * (0 = color 1, 255 = color 2)
     * @return The pixel int array representing the averaged color.
     */
    public static int[] getColorAverage(int[] pixel, Color c1, Color c2, double weight){
        pixel[0] = getChannelAverage(c1.getRed(), c2.getRed(), weight);
        pixel[1] = getChannelAverage(c1.getGreen(), c2.getGreen(), weight);
        pixel[2] = getChannelAverage(c1.getBlue(), c2.getBlue(), weight);
        return pixel;
    }

    public static void applyFadeFactor(BufferedImage img, double factor){
        int[] pixel = new int[]{0, 0, 0, 255};
        WritableRaster raster = img.getRaster();
        for(int y=0; y<img.getHeight(); y++){
            for(int x=0; x<img.getWidth(); x++){
                pixel = raster.getPixel(x, y, pixel);
                pixel[3] *= factor;
                raster.setPixel(x, y, pixel);
            }
        }
    }

    public static void normaliseDoubleMap(double[][] map){
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for(double[] line : map){
            for(double v : line){
                if (v > max) max = v;
                if (v < min) min = v;
            }
        }
        for(int y=0; y<map.length; y++){
            for(int x=0; x<map[y].length; x++){
                map[y][x] = (map[y][x]-min)/(max-min);
            }
        }
    }

    /**
     * Returns the value for an individual RGB channel during color averaging.
     * @param v1 the value of the channel in color 1.
     * @param v2 the value of the channel in color 2.
     * @param weight the weight of color 1 in the average (0 -> 1).
     */
    private static int getChannelAverage(double v1, double v2, double weight){
        return (int)(v1*(1D-weight)/2D + v2*(weight+1D)/2D);
    }

    /**
     * Compares the first 3 color channels of a pixel for equality.
     * Compatible only with RGB and not ARGB pixels.
     * @param p1 pixel 1
     * @param p2 pixel 2
     * @return true if the pixels are the same color.
     */
    public static boolean rgbPixelEquals(int[] p1, int[] p2){
        for(int n=0;n<3;n++) if(p1[n] != p2[n]) return false;
        return true;
    }

    /**
     * Compares the last 3 color channels of a pixel for equality.
     * Compatible only with ARGB and not RGB pixels.
     * @param p1 pixel 1
     * @param p2 pixel 2
     * @return true if the pixels are the same color.
     */
    public static boolean ARGBPixelEquals(int[] p1, int[] p2){
        for(int n=1;n<4;n++) if(p1[n] != p2[n]) return false;
        return true;
    }

    public static Color setBrightness(Color col, double factor){
        return new Color(
                (int) min(255, col.getRed()*factor),
                (int) min(255, col.getGreen()*factor),
                (int) min(255, col.getBlue()*factor)
        );
    }

}
