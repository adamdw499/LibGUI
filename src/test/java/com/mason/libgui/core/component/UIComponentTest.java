package com.mason.libgui.core.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.awt.*;

class UIComponentTest{


    private static class TestHitbox implements Hitbox {

        Coord lastSetCoord;
        boolean withinBoundsReturn = false;
        Coord withinBoundsArg;

        @Override
        public boolean withinBounds(Coord c) {
            withinBoundsArg = c;
            return withinBoundsReturn;
        }

        @Override
        public void setCoord(Coord c) {
            lastSetCoord = c;
        }

        @Override
        public Coord getCoord() {
            return lastSetCoord;
        }
    }

    private static class TestComponent extends UIComponent {

        boolean tickCalled = false;
        boolean renderCalled = false;
        Graphics2D lastGraphics;

        public TestComponent(Hitbox hitbox) {
            super(hitbox);
        }

        @Override
        public void render(Graphics2D g) {
            renderCalled = true;
            lastGraphics = g;
        }

        @Override
        public void tick() {
            tickCalled = true;
        }
    }

    @Test
    void withinBounds_delegatesToHitbox() {
        TestHitbox hitbox = new TestHitbox();
        hitbox.withinBoundsReturn = true;
        TestComponent component = new TestComponent(hitbox);

        Coord query = new Coord(10, 20);
        boolean result = component.withinBounds(query);

        assertTrue(result);
        assertEquals(query, hitbox.withinBoundsArg);
    }

    @Test
    void setCoord_delegatesToHitbox() {
        TestHitbox hitbox = new TestHitbox();
        TestComponent component = new TestComponent(hitbox);

        Coord newCoord = new Coord(5, 7);
        component.setCoord(newCoord);

        assertEquals(newCoord, hitbox.lastSetCoord);
    }

    @Test
    void getCoord_delegatesToHitbox() {
        TestHitbox hitbox = new TestHitbox();
        TestComponent component = new TestComponent(hitbox);

        Coord coord = new Coord(3, 4);
        hitbox.lastSetCoord = coord; // simulate hitbox internal state

        assertEquals(coord, component.getCoord());
    }

    @Test
    void tick_callsSubclassImplementation() {
        TestComponent component = new TestComponent(new TestHitbox());

        assertFalse(component.tickCalled);
        component.tick();
        assertTrue(component.tickCalled);
    }

    @Test
    void render_callsSubclassImplementation() {
        TestComponent component = new TestComponent(new TestHitbox());

        assertFalse(component.renderCalled);
        component.render(null);
        assertTrue(component.renderCalled);
        assertNull(component.lastGraphics);
    }

}