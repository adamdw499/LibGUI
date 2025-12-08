package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.components.behaviour.drag.AbstractDragBehaviour;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.Field;
import java.util.function.Consumer;

class ViewportInputDistributorTest{

    private static class TestViewportMouseInputCapturer extends ViewportMouseInputCapturer {
        private boolean active;

        void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public boolean isActive() {
            return active;
        }
    }

    private static class TestMouseListener implements BoundedMouseInputListener{
        final String name;
        boolean withinBounds;
        MouseInputEvent lastEvent;
        String lastMethod;

        TestMouseListener(String name, boolean withinBounds) {
            this.name = name;
            this.withinBounds = withinBounds;
        }

        @Override
        public boolean withinBounds(Coord c) {
            return withinBounds;
        }

        @Override
        public void onMouseMoved(MouseInputEvent e) { lastEvent = e; lastMethod = "moved"; }
        @Override
        public void onMouseDragged(MouseInputEvent e) { lastEvent = e; lastMethod = "dragged"; }
        @Override
        public void onMousePressed(MouseInputEvent e) { lastEvent = e; lastMethod = "pressed"; }
        @Override
        public void onMouseReleased(MouseInputEvent e) { lastEvent = e; lastMethod = "released"; }
        @Override
        public void onMouseClicked(MouseInputEvent e) { lastEvent = e; lastMethod = "clicked"; }
        @Override
        public void onMouseWheel(MouseInputEvent e) { lastEvent = e; lastMethod = "wheel"; }
    }

    // ---------- Helper constructors ----------

    private static MouseInputEvent mousePressedAt(int x, int y) {
        MouseEvent raw = new MouseEvent(
                new JButton(),
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                0,
                x, y,
                1,
                false
        );
        return new MouseInputEvent(raw);
    }

    private static MouseInputEvent wheelEventAt(int x, int y, int rotation) {
        MouseWheelEvent raw = new MouseWheelEvent(
                new JButton(),
                MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(),
                0,
                x, y,
                0,
                false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                rotation
        );
        return new MouseInputEvent(raw);
    }

    private static PanZoomBehaviour buildBehaviour(Rect bounding, Rect initialView,
                                                   Zoom zoom, ViewportMouseInputCapturer capturer) {
        Consumer<Graphics2D> renderable = g -> {};
        return PanZoomBehaviour.buildBehaviour(renderable, bounding, initialView, zoom, capturer);
    }

    private static double getCurrentlyDragging(AbstractDragBehaviour behaviour) throws Exception {
        Field f = AbstractDragBehaviour.class.getDeclaredField("currentlyDragging");
        f.setAccessible(true);
        return f.getBoolean(behaviour) ? 1.0 : 0.0;
    }

    // ---------- Tests ----------

    @Test
    void whenCapturerActive_eventIsSentToPanAndZoomBehaviour_withoutCoordinateConversion() throws Exception {
        // bounding / initialView don't matter much here
        Rect bounding = new Rect(0, 0, 100, 100);
        Rect initialView = new Rect(0, 0, 100, 100);

        Zoom zoom = Zoom.buildZoomWithDefaultNumZoomLevels(1.0, 16.0);

        TestViewportMouseInputCapturer capturer = new TestViewportMouseInputCapturer();
        capturer.setActive(true);

        PanZoomBehaviour behaviour = buildBehaviour(bounding, initialView, zoom, capturer);
        ViewportInputDistributor distributor = new ViewportInputDistributor(behaviour, capturer);
        behaviour.setInputSource(distributor);

        MouseInputEvent event = mousePressedAt(10, 20);
        Coord originalCoord = event.getCoord();

        // before: not dragging
        assertEquals(0.0, getCurrentlyDragging(behaviour));

        distributor.onMouseInput(event);

        // After: AbstractDragBehaviour should have handled the press and set currentlyDragging = true
        assertEquals(1.0, getCurrentlyDragging(behaviour),
                "Behaviour should have received the event when capturer is active");

        // Coordinates must not have been converted in this branch
        assertEquals(originalCoord, event.getCoord());
    }

    @Test
    void whenCapturerInactive_eventCoordIsConvertedToApparent_andSentToFirstMatchingMouseListener() {
        // Use a bounding rect with non-zero origin so we can see the translation
        Rect bounding = new Rect(10, 20, 100, 100);
        Rect initialView = bounding; // default pattern

        Zoom zoom = Zoom.buildZoomWithDefaultNumZoomLevels(1.0, 16.0); // zoom = 1.0 initially

        TestViewportMouseInputCapturer capturer = new TestViewportMouseInputCapturer();
        capturer.setActive(false);

        PanZoomBehaviour behaviour = buildBehaviour(bounding, initialView, zoom, capturer);
        ViewportInputDistributor distributor = new ViewportInputDistributor(behaviour, capturer);

        // Two listeners; both say "withinBounds", but due to descending iteration order,
        // the *last added* should receive the event
        TestMouseListener first = new TestMouseListener("first", true);
        TestMouseListener second = new TestMouseListener("second", true);

        distributor.addMouseInputListener(first);
        distributor.addMouseInputListener(second);

        // Screen coord (5,6). With viewport at topLeft=(10,20) and zoom=1,
        // screenToApparent = (5+10, 6+20) = (15,26)
        MouseInputEvent event = mousePressedAt(5, 6);
        Coord expectedScreen = new Coord(5, 6);

        distributor.onMouseInput(event);

        Coord expectedApparent = new Coord(15, 26);

        // Event coord should have been converted to apparent
        assertEquals(expectedScreen, event.getCoord(),
                "Event coord should remain unchanged");

        // Due to descending iterator, 'second' should see the event, not 'first'
        assertEquals("pressed", second.lastMethod);
        assertEquals(expectedApparent, second.lastEvent.getCoord());
        assertNull(first.lastMethod, "First listener should not receive the event (break after first match)");
    }

    @Test
    void whenCapturerInactive_mouseWheelAlsoConvertedAndDispatched() {
        Rect bounding = new Rect(10, 0, 100, 100);
        Rect initialView = bounding;
        Zoom zoom = Zoom.buildZoomWithDefaultNumZoomLevels(1.0, 16.0);

        TestViewportMouseInputCapturer capturer = new TestViewportMouseInputCapturer();
        capturer.setActive(false);

        PanZoomBehaviour behaviour = buildBehaviour(bounding, initialView, zoom, capturer);
        ViewportInputDistributor distributor = new ViewportInputDistributor(behaviour, capturer);

        TestMouseListener listener = new TestMouseListener("wheelListener", true);
        distributor.addMouseInputListener(listener);

        // Wheel event at (3,4) -> apparent = (3+10, 4+0) = (13,4)
        MouseInputEvent event = wheelEventAt(3, 4, -1);

        distributor.onMouseInput(event);

        assertEquals("wheel", listener.lastMethod);
        assertEquals(new Coord(13, 4), listener.lastEvent.getCoord());
        // ensure we didn't accidentally send it to behaviour (which would also handle wheel)
    }

}