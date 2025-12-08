package com.mason.libgui.utils.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

class RectTest{

    @Test
    void buildRect_topLeftBottomRight_inclusive() {
        Coord topLeft = new Coord(2, 3);
        Coord bottomRight = new Coord(4, 7);

        Rect rect = Rect.buildRect(topLeft, bottomRight);

        // width = 4 - 2 + 1 = 3, height = 7 - 3 + 1 = 5
        assertEquals(2, rect.x());
        assertEquals(3, rect.y());
        assertEquals(3, rect.width());
        assertEquals(5, rect.height());
    }

    @Test
    void buildRect_topLeftWidthHeight() {
        Coord topLeft = new Coord(1, 2);

        Rect rect = Rect.buildRect(topLeft, 5, 7);

        assertEquals(new Rect(1, 2, 5, 7), rect);
    }

    @Test
    void buildRect_topLeftSize() {
        Coord topLeft = new Coord(1, 2);
        Size size = new Size(5, 7);

        Rect rect = Rect.buildRect(topLeft, size);

        assertEquals(new Rect(1, 2, 5, 7), rect);
    }

    // ---------- Bounds & interior ----------

    @Test
    void withinBounds_halfOpenRectangle() {
        Rect rect = new Rect(10, 20, 5, 7); // x in [10,15), y in [20,27)

        Coord topLeft = new Coord(10, 20);     // inside
        Coord bottomRight = new Coord(14, 26); // inside (x+width-1, y+height-1)

        Coord leftOutside = new Coord(9, 20);
        Coord topOutside = new Coord(10, 19);
        Coord rightOutside = new Coord(15, 20);   // x = x+width
        Coord bottomOutside = new Coord(10, 27);  // y = y+height

        assertTrue(rect.withinBounds(topLeft));
        assertTrue(rect.withinBounds(bottomRight));

        assertFalse(rect.withinBounds(leftOutside));
        assertFalse(rect.withinBounds(topOutside));
        assertFalse(rect.withinBounds(rightOutside));
        assertFalse(rect.withinBounds(bottomOutside));
    }

    @Test
    void withinInterior_strictInterior() {
        Rect rect = new Rect(10, 20, 5, 7); // x in [10,14], y in [20,26] for corners

        Coord interior = new Coord(11, 21);         // strictly inside
        Coord onLeftBorder = new Coord(10, 23);
        Coord onRightBorder = new Coord(14, 23);
        Coord onTopBorder = new Coord(12, 20);
        Coord onBottomBorder = new Coord(12, 26);
        Coord corner = new Coord(10, 20);

        assertTrue(rect.withinInterior(interior));

        assertFalse(rect.withinInterior(onLeftBorder));
        assertFalse(rect.withinInterior(onRightBorder));
        assertFalse(rect.withinInterior(onTopBorder));
        assertFalse(rect.withinInterior(onBottomBorder));
        assertFalse(rect.withinInterior(corner));
    }

    @Test
    void interiorImpliesBounds() {
        Rect rect = new Rect(10, 20, 5, 7);
        Coord interior = new Coord(11, 21);

        assertTrue(rect.withinInterior(interior));
        assertTrue(rect.withinBounds(interior));
    }

    // ---------- RectQuery methods ----------

    @Test
    void getCoord_returnsTopLeft() {
        Rect rect = new Rect(3, 4, 5, 6);

        assertEquals(new Coord(3, 4), rect.getCoord());
        assertEquals(rect.topLeft(), rect.getCoord());
    }

    @Test
    void getSize_returnsSize() {
        Rect rect = new Rect(3, 4, 5, 7);

        Size size = rect.getSize();

        assertEquals(5, size.width());
        assertEquals(7, size.height());
    }

    // ---------- Coordinate helpers ----------

    @Test
    void cornerMethods() {
        Rect rect = new Rect(10, 20, 5, 7);

        assertEquals(new Coord(10, 20), rect.topLeft());
        assertEquals(new Coord(14, 20), rect.topRight());      // x + width - 1
        assertEquals(new Coord(10, 26), rect.bottomLeft());    // y + height - 1
        assertEquals(new Coord(14, 26), rect.bottomRight());
    }

    @Test
    void midpointMethods() {
        Rect rect = new Rect(10, 20, 5, 7);
        // width/2 = 2, height/2 = 3 (integer division)

        assertEquals(new Coord(12, 20), rect.topMid());        // (x + 2, y)
        assertEquals(new Coord(12, 26), rect.bottomMid());     // (x + 2, y + 6)
        assertEquals(new Coord(10, 23), rect.leftMid());       // (x, y + 3)
        assertEquals(new Coord(14, 23), rect.rightMid());      // (x + 4, y + 3)
    }

    @Test
    void centreMethod() {
        Rect rect = new Rect(10, 20, 5, 7);
        // centre = (x + width/2, y + height/2) = (10 + 2, 20 + 3) = (12, 23)

        Coord centre = rect.centre();

        assertEquals(new Coord(12, 23), centre);
        assertTrue(rect.withinBounds(centre), "Centre should be within bounds");
    }

}