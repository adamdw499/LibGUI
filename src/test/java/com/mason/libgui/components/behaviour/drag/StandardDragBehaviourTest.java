package com.mason.libgui.components.behaviour.drag;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class StandardDragBehaviourTest{

    private static class TestHitbox implements Hitbox{
        Coord coord = new Coord(0, 0);
        boolean withinBoundsReturn = false;
        Coord lastWithinBoundsArg;

        @Override
        public boolean withinBounds(Coord c) {
            lastWithinBoundsArg = c;
            return withinBoundsReturn;
        }

        @Override
        public void setCoord(Coord c) {
            coord = c;
        }

        @Override
        public Coord getCoord() {
            return coord;
        }
    }

    private MouseInputEvent mouseInputEvent(int x, int y) {
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

    // ---------- Tests ----------

    @Test
    void withinBounds_delegatesToDragRegion() {
        TestHitbox hitbox = new TestHitbox();
        hitbox.withinBoundsReturn = true;

        StandardDragBehaviour behaviour = new StandardDragBehaviour(hitbox);

        Coord p = new Coord(10, 20);
        boolean result = behaviour.withinBounds(p);

        assertTrue(result);
        assertEquals(p, hitbox.lastWithinBoundsArg);

        hitbox.withinBoundsReturn = false;
        assertFalse(behaviour.withinBounds(p));
    }

    @Test
    void getDragRegion_returnsSameHitboxInstance() {
        TestHitbox hitbox = new TestHitbox();
        StandardDragBehaviour behaviour = new StandardDragBehaviour(hitbox);

        assertSame(hitbox, behaviour.getDragRegion());
    }

    @Test
    void onDragStart_setsInitialOffset_mouseMinusRegionCoord() throws Exception {
        TestHitbox hitbox = new TestHitbox();
        hitbox.coord = new Coord(10, 20);

        StandardDragBehaviour behaviour = new StandardDragBehaviour(hitbox);

        Coord mouse = new Coord(13, 29);
        MouseInputEvent event = mouseInputEvent(mouse.x(), mouse.y());

        Method onDragStart = StandardDragBehaviour.class
                .getDeclaredMethod("onDragStart", MouseInputEvent.class);
        onDragStart.setAccessible(true);
        onDragStart.invoke(behaviour, event);

        Field offsetField = StandardDragBehaviour.class
                .getDeclaredField("initialDragOffset");
        offsetField.setAccessible(true);
        Coord offset = (Coord) offsetField.get(behaviour);

        // offset = mouse - regionCoord = (13-10, 29-20) = (3,9)
        assertEquals(new Coord(3, 9), offset);
    }

    @Test
    void onDragIncrement_movesRegionKeepingInitialOffset() throws Exception {
        TestHitbox hitbox = new TestHitbox();
        hitbox.coord = new Coord(10, 20); // initial region position

        StandardDragBehaviour behaviour = new StandardDragBehaviour(hitbox);

        // Simulate drag start at mouse (15,25) -> offset = (5,5)
        MouseInputEvent startEvent = mouseInputEvent(15, 25);
        Method onDragStart = StandardDragBehaviour.class
                .getDeclaredMethod("onDragStart", MouseInputEvent.class);
        onDragStart.setAccessible(true);
        onDragStart.invoke(behaviour, startEvent);

        // Now drag mouse to (40,50) -> new coord = (40-5, 50-5) = (35,45)
        MouseInputEvent incrementEvent = mouseInputEvent(40, 50);
        Method onDragIncrement = StandardDragBehaviour.class
                .getDeclaredMethod("onDragIncrement", MouseInputEvent.class);
        onDragIncrement.setAccessible(true);
        onDragIncrement.invoke(behaviour, incrementEvent);

        assertEquals(new Coord(35, 45), hitbox.coord);
    }

    @Test
    void onDragRelease_doesNotChangeDragRegion() throws Exception {
        TestHitbox hitbox = new TestHitbox();
        hitbox.coord = new Coord(5, 7);

        StandardDragBehaviour behaviour = new StandardDragBehaviour(hitbox);

        Coord before = hitbox.coord;

        MouseInputEvent releaseEvent = mouseInputEvent(20, 30);
        Method onDragRelease = StandardDragBehaviour.class
                .getDeclaredMethod("onDragRelease", MouseInputEvent.class);
        onDragRelease.setAccessible(true);
        onDragRelease.invoke(behaviour, releaseEvent);

        // Body is empty; region position should be unchanged
        assertEquals(before, hitbox.coord);
    }

}