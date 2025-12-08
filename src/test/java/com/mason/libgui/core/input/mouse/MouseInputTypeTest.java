package com.mason.libgui.core.input.mouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class MouseInputTypeTest{

    private JButton source() {
        return new JButton();
    }

    @Test
    void fromRawMouseEvent_mouseEntered_mapsToEnter() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_ENTERED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        assertEquals(MouseInputType.ENTER, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseExited_mapsToExit() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_EXITED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        assertEquals(MouseInputType.EXIT, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseMoved_mapsToMove() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        assertEquals(MouseInputType.MOVE, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseDragged_mapsToDrag() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_DRAGGED,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        assertEquals(MouseInputType.DRAG, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mousePressed_mapsToPress() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                0,
                10, 20,
                1,
                false
        );

        assertEquals(MouseInputType.PRESS, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseReleased_mapsToRelease() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(),
                0,
                10, 20,
                1,
                false
        );

        assertEquals(MouseInputType.RELEASE, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseClicked_mapsToClick() {
        MouseEvent e = new MouseEvent(
                source(),
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10, 20,
                1,
                false
        );

        assertEquals(MouseInputType.CLICK, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_mouseWheelMoved_mapsToWheel() {
        MouseWheelEvent e = new MouseWheelEvent(
                source(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                10, 20,
                0,  // clickCount
                false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                1
        );

        assertEquals(MouseInputType.WHEEL, MouseInputType.fromRawMouseEvent(e));
    }

    @Test
    void fromRawMouseEvent_unexpectedId_throwsIllegalStateException() {
        MouseEvent e = new MouseEvent(
                source(),
                9999, // not one of the handled IDs
                System.currentTimeMillis(),
                0,
                10, 20,
                0,
                false
        );

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> MouseInputType.fromRawMouseEvent(e)
        );
        assertTrue(ex.getMessage().contains("9999"));
    }

}