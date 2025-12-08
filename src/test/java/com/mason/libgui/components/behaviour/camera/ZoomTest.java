package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class ZoomTest{

    private static final double EPS = 1e-9;

    private MouseInputEvent wheelEvent(int rotation) {
        MouseWheelEvent raw = new MouseWheelEvent(
                new JButton(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                0, 0,
                0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                rotation
        );
        return new MouseInputEvent(raw);
    }

    @Test
    void mouseWheel_negativeTicks_zoomInWithZoomFactor() {
        double min = 1.0;
        double max = 16.0;
        int levels = 5;
        double initial = 4.0;

        Zoom zoom = Zoom.buildZoom(min, max, levels, initial);
        double zoomFactor = Math.pow(max / min, 1.0 / (levels - 1));

        // negative rotation => zoom in (multiply by zoomFactor)
        zoom.mouseWheel(wheelEvent(-1));
        assertEquals(initial * zoomFactor, zoom.getZoom(), EPS);

        // positive rotation => zoom out (divide by zoomFactor)
        zoom.mouseWheel(wheelEvent(1)); // back to original
        assertEquals(initial, zoom.getZoom(), EPS);
    }

    @Test
    void mouseWheel_zeroTicks_keepsZoomUnchanged() {
        Zoom zoom = Zoom.buildZoom(1.0, 16.0, 5, 3.5);
        double before = zoom.getZoom();

        zoom.mouseWheel(wheelEvent(0));

        assertEquals(before, zoom.getZoom(), EPS);
    }

    @Test
    void buildZoom_usesNumZoomLevelsCorrectly_minToMaxInNMinus1Steps() {
        double min = 1.0;
        double max = 16.0;
        int levels = 5; // so N-1 = 4 steps from min to max
        Zoom zoom = Zoom.buildZoom(min, max, levels, min);

        // With rotation -4, we should zoom in from min to max
        zoom.mouseWheel(wheelEvent(-4));
        assertEquals(max, zoom.getZoom(), EPS);

        // And back down to min with +4
        zoom.mouseWheel(wheelEvent(4));
        assertEquals(min, zoom.getZoom(), EPS);
    }

    @Test
    void mouseWheel_clampsAtMaxZoom() {
        double min = 1.0;
        double max = 16.0;
        Zoom zoom = Zoom.buildZoom(min, max, 5, 2.0);

        // Huge negative rotation → theoretically would exceed max
        zoom.mouseWheel(wheelEvent(-100));
        assertEquals(max, zoom.getZoom(), EPS);
    }

    @Test
    void mouseWheel_clampsAtMinZoom() {
        double min = 1.0;
        double max = 16.0;
        Zoom zoom = Zoom.buildZoom(min, max, 5, max);

        // Huge positive rotation → theoretically would go below min
        zoom.mouseWheel(wheelEvent(100));
        assertEquals(min, zoom.getZoom(), EPS);
    }

    @Test
    void buildZoomWithDefaultNumZoomLevels_hasInitialZoomOne_andClampsToGivenRange() {
        double min = 1.0 / 16.0;
        double max = 16.0;
        Zoom zoom = Zoom.buildZoomWithDefaultNumZoomLevels(min, max);

        // initial zoom should be 1
        assertEquals(1.0, zoom.getZoom(), EPS);

        // zoom out a lot -> clamp at min
        zoom.mouseWheel(wheelEvent(+100));
        assertEquals(min, zoom.getZoom(), EPS);

        // zoom in a lot -> clamp at max
        zoom.mouseWheel(wheelEvent(-100));
        assertEquals(max, zoom.getZoom(), EPS);
    }

    @Test
    void buildDefaultMiddleZoom_usesExpectedRangeAndInitialZoom() {
        Zoom zoom = Zoom.buildDefaultMiddleZoom(); // min = 1/16, max = 16, initial = 1

        assertEquals(1.0, zoom.getZoom(), EPS);

        // Zoom out heavily → min = 1/16
        zoom.mouseWheel(wheelEvent(+100));
        assertEquals(1.0 / 16.0, zoom.getZoom(), EPS);

        // Zoom in heavily → max = 16
        zoom.mouseWheel(wheelEvent(-100));
        assertEquals(16.0, zoom.getZoom(), EPS);
    }

    @Test
    void buildDefaultFullyZoomedOutZoom_initiallyMinAndCanZoomInToMax() {
        Zoom zoom = Zoom.buildDefaultFullyZoomedOutZoom(); // min = 1, max = 16*16 = 256, initial = 1

        // Initially at min
        assertEquals(1.0, zoom.getZoom(), EPS);

        // Zoom out shouldn't go below min
        zoom.mouseWheel(wheelEvent(+10));
        assertEquals(1.0, zoom.getZoom(), EPS);

        // Zoom in heavily up to max
        zoom.mouseWheel(wheelEvent(-100));
        assertEquals(256.0, zoom.getZoom(), EPS);
    }

}