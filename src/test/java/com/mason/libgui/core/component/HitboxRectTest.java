package com.mason.libgui.core.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

class HitboxRectTest{

    @Test
    void constructor_setsTopLeftAndSize() {
        Coord topLeft = new Coord(10, 20);
        Size size = new Size(5, 7);

        HitboxRect hb = new HitboxRect(topLeft, size);

        assertEquals(topLeft, hb.getCoord());
        assertEquals(size, hb.getSize());
    }

    @Test
    void toRect_buildsRectFromTopLeftAndSize() {
        Coord topLeft = new Coord(3, 4);
        Size size = new Size(10, 20);
        HitboxRect hb = new HitboxRect(topLeft, size);

        Rect expected = Rect.buildRect(topLeft, size);

        assertEquals(expected, hb.toRect());
    }

    @Test
    void withinBounds_usesRectSemantics() {
        Coord topLeft = new Coord(10, 20);
        Size size = new Size(5, 7); // x in [10,15), y in [20,27)
        HitboxRect hb = new HitboxRect(topLeft, size);

        Coord inside = new Coord(12, 22);
        Coord boundaryX = new Coord(15, 22); // x == 10 + 5
        Coord boundaryY = new Coord(12, 27); // y == 20 + 7
        Coord outside = new Coord(100, 100);

        assertTrue(hb.withinBounds(inside));
        assertFalse(hb.withinBounds(boundaryX));
        assertFalse(hb.withinBounds(boundaryY));
        assertFalse(hb.withinBounds(outside));
    }

    @Test
    void setCoord_movesHitbox() {
        HitboxRect hb = new HitboxRect(new Coord(0, 0), new Size(5, 5));

        assertTrue(hb.withinBounds(new Coord(2, 2)));

        hb.setCoord(new Coord(10, 10));

        assertFalse(hb.withinBounds(new Coord(2, 2)));
        assertTrue(hb.withinBounds(new Coord(12, 12)));
    }

    @Test
    void setSize_changesBounds() {
        HitboxRect hb = new HitboxRect(new Coord(0, 0), new Size(10, 10));

        // Initially inside
        assertTrue(hb.withinBounds(new Coord(9, 9)));

        hb.setSize(new Size(5, 5));

        // Now outside with smaller size
        assertFalse(hb.withinBounds(new Coord(9, 9)));
        assertTrue(hb.withinBounds(new Coord(4, 4)));
    }

    // ---------- clampWithinBoundary ----------

    @Test
    void clampWithinBoundary_doesNothingWhenAlreadyInside() {
        HitboxRect hb = new HitboxRect(new Coord(10, 20), new Size(5, 5));
        Rect boundary = new Rect(0, 0, 100, 100);

        hb.clampWithinBoundary(boundary);

        assertEquals(new Coord(10, 20), hb.getCoord());
    }

    @Test
    void clampWithinBoundary_clampsLeftAndTop() {
        HitboxRect hb = new HitboxRect(new Coord(-10, -20), new Size(5, 5));
        Rect boundary = new Rect(0, 0, 100, 100);

        hb.clampWithinBoundary(boundary);

        // x,y less than boundary => clamped to boundary's top-left
        assertEquals(new Coord(0, 0), hb.getCoord());
    }

    @Test
    void clampWithinBoundary_clampsRightAndBottomToBoundaryPlusSize() {
        HitboxRect hb = new HitboxRect(new Coord(200, 300), new Size(5, 5));
        Rect boundary = new Rect(10, 20, 100, 100);

        hb.clampWithinBoundary(boundary);

        // Note: implementation uses boundaryX + boundaryWidth (no -1)
        assertEquals(new Coord(105, 115), hb.getCoord());
    }

    @Test
    void clampWithinBoundary_clampsIndependentlyOnEachAxis() {
        HitboxRect hb = new HitboxRect(new Coord(-5, 500), new Size(5, 5));
        Rect boundary = new Rect(10, 20, 100, 100);

        hb.clampWithinBoundary(boundary);

        // x < boundary => x = 10
        // y > boundaryY + height => y = 20 + 100 = 120
        assertEquals(new Coord(10, 115), hb.getCoord());
    }

    @Test
    void clampWithinBoundary_usesBoundaryRectQueryInterface() {
        HitboxRect hb = new HitboxRect(new Coord(50, 50), new Size(5, 5));

        RectQuery boundary = Rect.buildRect(new Coord(0, 0), new Size(20, 30));

        hb.clampWithinBoundary(boundary);

        assertEquals(new Coord(15, 25), hb.getCoord());
    }

}