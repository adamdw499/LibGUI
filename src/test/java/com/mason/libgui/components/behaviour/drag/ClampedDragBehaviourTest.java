package com.mason.libgui.components.behaviour.drag;

import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

class ClampedDragBehaviourTest {

    private MouseInputEvent mouseEvent(int x, int y) {
        MouseEvent raw = new MouseEvent(
                new JButton(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false
        );
        return new MouseInputEvent(raw);
    }

    private void invokeOnDragStart(StandardDragBehaviour behaviour, MouseInputEvent event)
            throws Exception {
        Method m = StandardDragBehaviour.class
                .getDeclaredMethod("onDragStart", MouseInputEvent.class);
        m.setAccessible(true);
        m.invoke(behaviour, event);
    }

    @Test
    void onDragIncrement_movesRegionNormally_whenResultInsideClamp() throws Exception {
        // Drag region starts at (10,20), size irrelevant to this test
        HitboxRect dragRect = new HitboxRect(new Coord(10, 20), new Size(5, 5));
        Rect clampRect = new Rect(0, 0, 100, 100); // generous boundary

        ClampedDragBehaviour behaviour = new ClampedDragBehaviour(dragRect, clampRect);

        // Simulate drag start at mouse (15,25):
        // offset = mouse - regionCoord = (15-10, 25-20) = (5,5)
        MouseInputEvent startEvent = mouseEvent(15, 25);
        invokeOnDragStart(behaviour, startEvent);

        // Now drag mouse to (40,50):
        // StandardDragBehaviour newCoord = (40-5, 50-5) = (35,45), which is well inside clampRect
        MouseInputEvent dragEvent = mouseEvent(40, 50);
        behaviour.onDragIncrement(dragEvent);

        assertEquals(new Coord(35, 45), dragRect.getCoord(),
                "Drag region should move according to standard drag logic when inside clamp");
    }

    @Test
    void onDragIncrement_clampsRegionWhenNewCoordOutsideClampRect() throws Exception {
        // Drag region starts at (0,0)
        HitboxRect dragRect = new HitboxRect(new Coord(0, 0), new Size(5, 5));
        Rect clampRect = new Rect(0, 0, 20, 20); // boundary from (0,0) to (20,20) per clamping logic

        ClampedDragBehaviour behaviour = new ClampedDragBehaviour(dragRect, clampRect);

        // Start drag at mouse (2,3): offset = (2,3)
        MouseInputEvent startEvent = mouseEvent(2, 3);
        invokeOnDragStart(behaviour, startEvent);

        // Drag mouse far outside clamp rect: (50,60)
        // StandardDragBehaviour newCoord = (50-2, 60-3) = (48,57)
        // HitboxRect.clampWithinBoundary will clamp to:
        //   x = boundaryX + boundaryWidth = 0 + 20 = 20
        //   y = boundaryY + boundaryHeight = 0 + 20 = 20
        MouseInputEvent dragEvent = mouseEvent(50, 60);
        behaviour.onDragIncrement(dragEvent);

        assertEquals(new Coord(15, 15), dragRect.getCoord(),
                "Drag region should be clamped to the clampRect boundary");
    }

    @Test
    void onDragIncrement_clampsIndependentlyOnEachAxis() throws Exception {
        // Drag region starts at (10,10), clampRect from (0,0) to (20,20)
        HitboxRect dragRect = new HitboxRect(new Coord(10, 10), new Size(5, 5));
        Rect clampRect = new Rect(0, 0, 20, 20);

        ClampedDragBehaviour behaviour = new ClampedDragBehaviour(dragRect, clampRect);

        // Start drag at mouse (12, 8):
        // offset = (12-10, 8-10) = (2, -2)
        MouseInputEvent startEvent = mouseEvent(12, 8);
        invokeOnDragStart(behaviour, startEvent);

        // Drag mouse to (-5, 100):
        // Standard newCoord = (-5-2, 100-(-2)) = (-7, 102)
        // Clamping:
        //   x < 0  -> x = 0
        //   y > 20 -> y = 20
        MouseInputEvent dragEvent = mouseEvent(-5, 100);
        behaviour.onDragIncrement(dragEvent);

        assertEquals(new Coord(0, 15), dragRect.getCoord(),
                "Drag region should be clamped separately on X and Y axes");
    }

}