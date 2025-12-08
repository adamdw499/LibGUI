package com.mason.libgui.utils.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

class CoordTest {

    // ---------- midpoint ----------

    @Test
    void midpoint_ofSamePoint_isThatPoint() {
        Coord a = new Coord(3, 5);

        Coord mid = Coord.midpoint(a, a);

        assertEquals(new Coord(3, 5), mid);
    }

    @Test
    void midpoint_simpleEvenDistance() {
        Coord a = new Coord(0, 0);
        Coord b = new Coord(4, 6);

        Coord mid = Coord.midpoint(a, b);

        // (0+4)/2 = 2, (0+6)/2 = 3
        assertEquals(new Coord(2, 3), mid);
    }

    @Test
    void midpoint_oddDistance_usesIntegerDivision() {
        Coord a = new Coord(0, 0);
        Coord b = new Coord(1, 1);

        Coord mid = Coord.midpoint(a, b);

        // (0+1)/2 = 0
        assertEquals(new Coord(0, 0), mid);
    }

    @Test
    void midpoint_withNegativeCoordinates() {
        Coord a = new Coord(-2, -2);
        Coord b = new Coord(2, 2);

        Coord mid = Coord.midpoint(a, b);

        // (-2+2)/2 = 0
        assertEquals(new Coord(0, 0), mid);
    }

    // ---------- clampToRect ----------

    @Test
    void clampToRect_pointInsideRect_returnsSamePoint() {
        Rect rect = new Rect(10, 20, 5, 7); // x:10..15, y:20..27 inclusive for clamp logic
        Coord c = new Coord(12, 23);

        Coord clamped = c.clampToRect(rect);

        assertSame(c, clamped, "Inside point should return this (same instance) per current implementation");
        assertEquals(new Coord(12, 23), clamped);
    }

    @Test
    void clampToRect_pointLeftOfRect_clampsToLeftEdge() {
        Rect rect = new Rect(10, 20, 5, 7);
        Coord c = new Coord(5, 23);

        Coord clamped = c.clampToRect(rect);

        assertEquals(new Coord(10, 23), clamped);
    }

    @Test
    void clampToRect_pointRightOfRect_clampsToRightEdgeInclusive() {
        Rect rect = new Rect(10, 20, 5, 7); // x + width = 15
        Coord c = new Coord(25, 23);

        Coord clamped = c.clampToRect(rect);

        // Note: clamp logic allows x == rect.x() + rect.width()
        assertEquals(new Coord(15, 23), clamped);
    }

    @Test
    void clampToRect_pointAboveRect_clampsToTopEdge() {
        Rect rect = new Rect(10, 20, 5, 7);
        Coord c = new Coord(12, 10);

        Coord clamped = c.clampToRect(rect);

        assertEquals(new Coord(12, 20), clamped);
    }

    @Test
    void clampToRect_pointBelowRect_clampsToBottomEdgeInclusive() {
        Rect rect = new Rect(10, 20, 5, 7); // y + height = 27
        Coord c = new Coord(12, 40);

        Coord clamped = c.clampToRect(rect);

        // Note: clamp logic allows y == rect.y() + rect.height()
        assertEquals(new Coord(12, 27), clamped);
    }

    @Test
    void clampToRect_pointOutsideBothAxes_clampsToNearestCorner() {
        Rect rect = new Rect(10, 20, 5, 7); // x:10..15, y:20..27 (for clamp)
        Coord c = new Coord(0, 0);

        Coord clamped = c.clampToRect(rect);

        // Nearest corner: (10,20)
        assertEquals(new Coord(10, 20), clamped);
    }

    @Test
    void clampToRect_pointOutsideBothAxes_bottomRight() {
        Rect rect = new Rect(10, 20, 5, 7); // x:10..15, y:20..27 (for clamp)
        Coord c = new Coord(100, 100);

        Coord clamped = c.clampToRect(rect);

        // Nearest corner: (15,27) according to current clamp implementation
        assertEquals(new Coord(15, 27), clamped);
    }

}