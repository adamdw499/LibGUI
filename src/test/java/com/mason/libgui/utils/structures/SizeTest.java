package com.mason.libgui.utils.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SizeTest{

    @Test
    void constructor_withPositiveDimensions_createsSize() {
        Size size = new Size(3, 4);

        assertEquals(3, size.width());
        assertEquals(4, size.height());
    }

    @Test
    void constructor_withZeroWidth_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Size(0, 5)
        );
        assertTrue(ex.getMessage().contains("non-positive"));
    }

    @Test
    void constructor_withZeroHeight_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Size(5, 0)
        );
        assertTrue(ex.getMessage().contains("non-positive"));
    }

    @Test
    void constructor_withNegativeDimensions_throwsIllegalArgumentException() {
        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> new Size(-1, 5)
        );
        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> new Size(5, -1)
        );

        assertTrue(ex1.getMessage().contains("non-positive"));
        assertTrue(ex2.getMessage().contains("non-positive"));
    }

    @Test
    void toRect_behaviourMatchesManualRect() {
        Size size = new Size(10, 20);
        Rect expectedRect = new Rect(0, 0, 10, 20);

        Coord inside = new Coord(3, 4);
        Coord outside = new Coord(15, 25);

        // We don’t rely on Rect.equals(), only on withinBounds behaviour
        assertEquals(
                expectedRect.withinBounds(inside),
                size.toRect().withinBounds(inside),
                "toRect().withinBounds should match a manually constructed Rect for an inside point"
        );

        assertEquals(
                expectedRect.withinBounds(outside),
                size.toRect().withinBounds(outside),
                "toRect().withinBounds should match a manually constructed Rect for an outside point"
        );
    }

    @Test
    void withinBounds_delegatesToRect() {
        Size size = new Size(10, 20);
        Rect rectFromSize = size.toRect();

        Coord origin = new Coord(0, 0);
        Coord somePoint = new Coord(5, 10);
        Coord farOutside = new Coord(-3, 50);

        assertEquals(rectFromSize.withinBounds(origin), size.withinBounds(origin));
        assertEquals(rectFromSize.withinBounds(somePoint), size.withinBounds(somePoint));
        assertEquals(rectFromSize.withinBounds(farOutside), size.withinBounds(farOutside));
    }

}