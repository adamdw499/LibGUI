package com.mason.libgui.test.testComponents;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.DefaultRenderUtils;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A component that functions like a thick green border.
 */
public class PaneBorderTest extends UIComponent{


    private static final StyleInfo info;

    static{
        Color fore = new Color(75, 32, 32);
        Color darkGrey = new Color(22,42,22);
        Color grey = new Color(80, 120, 80);
        Color foreHighlight = new Color(105,62,62);
        Color fontCol = new Color(240,170,40);
        Font font = new Font("Dubstep Dungeons", Font.PLAIN, 18);
        Font title = new Font("Joystix Monospace", Font.PLAIN, 24);
        DefaultRenderUtils utils = new DefaultRenderUtils(12);

        info = new StyleInfo(fore, darkGrey, grey, foreHighlight, fontCol, font, title, utils);
    }

    public PaneBorderTest(int x, int y, int w, int h){
        super(x, y, w, h);
    }


    @Override
    public void render(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, info, x, y, width, height);
    }

    @Override
    public void tick(int mx, int my){}

    @Override
    public boolean withinBounds(int mx, int my){
        return super.withinBounds(mx, my) && (
                (mx <= x + info.getLineWidth() || mx >= x + width - info.getLineWidth())
                || (my <= y + info.getLineWidth() || my >= y + height - info.getLineWidth())
                );
    }

}
