package com.mason.libgui.core.input.mouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.Field;

class MouseInputEventTest{

    private Component source() {
        return new JButton();
    }

    @Test
    void constructor_setsTypeFromMouseEventAndCoordFromPosition() {
        MouseEvent raw = new MouseEvent(
                source(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        MouseInputEvent event = new MouseInputEvent(raw);

        assertEquals(new Coord(10, 20), event.getCoord());
        assertEquals(MouseInputType.MOVE, event.getType());
    }

    @Test
    void setCoordRelativeTo_updatesInternalCoordField() throws Exception {
        MouseEvent raw = new MouseEvent(
                source(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                15, 25,
                0,
                false
        );
        MouseInputEvent event = new MouseInputEvent(raw);

        // Make coord relative to (10,20) → expected (5,5)
        Coord topLeft = new Coord(10, 20);
        event.setCoordRelativeTo(topLeft);

        // Use reflection to inspect private coord field
        Field coordField = MouseInputEvent.class.getDeclaredField("coord");
        coordField.setAccessible(true);
        Coord internalCoord = (Coord) coordField.get(event);

        assertEquals(new Coord(5, 5), internalCoord);
    }

    @Test
    void getCoord_returnsCurrentMouseEventCoordinates_evenAfterRelativeChange() {
        MouseEvent raw = new MouseEvent(
                source(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                15, 25,
                0,
                false
        );
        MouseInputEvent event = new MouseInputEvent(raw);

        event.setCoordRelativeTo(new Coord(10, 20));

        assertEquals(new Coord(5, 5), event.getCoord());
    }

    @Test
    void getWheelMotion_returnsWheelRotationForWheelEvents() {
        MouseWheelEvent raw = new MouseWheelEvent(
                source(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,               // click count
                false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,               // scroll amount
                -2               // wheel rotation
        );

        MouseInputEvent event = new MouseInputEvent(raw);

        assertEquals(MouseInputType.WHEEL, event.getType());
        assertEquals(-2, event.getWheelMotion());
    }

    @Test
    void getWheelMotion_onNonWheelEventThrowsIllegalStateException() {
        MouseEvent raw = new MouseEvent(
                source(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        MouseInputEvent event = new MouseInputEvent(raw);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                event::getWheelMotion
        );
        assertTrue(ex.getMessage().contains("non-wheel"));
    }

}