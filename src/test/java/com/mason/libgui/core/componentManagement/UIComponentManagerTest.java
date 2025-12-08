package com.mason.libgui.core.componentManagement;

import com.mason.libgui.core.component.Hitbox;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.input.componentLayer.GUIInputDistributor;
import com.mason.libgui.core.input.componentLayer.UIComponentManagerInputDistributor;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

class UIComponentManagerTest{

    private static class TestHitbox implements Hitbox{
        Coord coord = new Coord(0, 0);
        boolean withinBoundsReturn = true;

        @Override
        public boolean withinBounds(Coord c) {
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

    private static class TestComponent extends UIComponent{

        boolean tickCalled = false;
        boolean renderCalled = false;
        Graphics2D lastGraphics;

        TestComponent() {
            super(new TestHitbox());
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

    private static class TestGUIInputDistributor implements GUIInputDistributor<BoundedMouseInputListener>{

        BoundedMouseInputListener lastAddedMouseListener;
        BoundedMouseInputListener lastRemovedMouseListener;
        KeyListener lastAddedKeyListener;
        KeyListener lastRemovedKeyListener;

        @Override
        public void addMouseInputListener(BoundedMouseInputListener listener) {
            lastAddedMouseListener = listener;
        }

        @Override
        public void removeMouseInputListener(BoundedMouseInputListener listener) {
            lastRemovedMouseListener = listener;
        }

        @Override
        public void addKeyListener(KeyListener listener) {
            lastAddedKeyListener = listener;
        }

        @Override
        public void removeKeyListener(KeyListener listener) {
            lastRemovedKeyListener = listener;
        }
    }

    private static class TestIterableUIComponentContainer implements IterableUIComponentContainer {

        final ArrayList<UIComponent> components = new ArrayList<>();

        @Override
        public Iterator<UIComponent> iterator() {
            return components.iterator();
        }

        @Override
        public void addComponent(UIComponent comp) {
            components.add(comp);
        }

        @Override
        public void removeComponent(UIComponent comp) {
            components.remove(comp);
        }
    }

    // ---------- buildUIComponentManager / constructor wiring ----------

    @Test
    void buildUIComponentManager_usesProvidedContainerAndDistributor() {
        TestIterableUIComponentContainer container = new TestIterableUIComponentContainer();
        TestGUIInputDistributor distributor = new TestGUIInputDistributor();

        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        // add/remove components delegate to container
        TestComponent c1 = new TestComponent();
        manager.addComponent(c1);
        assertTrue(container.components.contains(c1));

        manager.removeComponent(c1);
        assertFalse(container.components.contains(c1));

        // input registration delegates to distributor
        BoundedMouseInputListener mouseListener = new TestBoundedMouseInputListener();
        KeyListener keyListener = new TestKeyListener();

        manager.addMouseInputListener(mouseListener);
        assertSame(mouseListener, distributor.lastAddedMouseListener);

        manager.removeMouseInputListener(mouseListener);
        assertSame(mouseListener, distributor.lastRemovedMouseListener);

        manager.addKeyListener(keyListener);
        assertSame(keyListener, distributor.lastAddedKeyListener);

        manager.removeKeyListener(keyListener);
        assertSame(keyListener, distributor.lastRemovedKeyListener);

        // getInputDistributor returns same instance
        assertSame(distributor, manager.getInputDistributor());
    }

    // ---------- Simple builder ----------

    @Test
    void buildSimpleUIComponentManager_createsWorkingManager() {
        UIComponentManager manager = UIComponentManager.buildSimpleUIComponentManager();

        // Can add a component and tick/render it
        TestComponent c = new TestComponent();
        manager.addComponent(c);

        assertFalse(c.tickCalled);
        manager.tickComponents();
        assertTrue(c.tickCalled);

        assertFalse(c.renderCalled);
        manager.renderComponents(null);
        assertTrue(c.renderCalled);

        // Input distributor exists and is of expected type
        assertNotNull(manager.getInputDistributor());
        assertTrue(manager.getInputDistributor() instanceof UIComponentManagerInputDistributor);
    }

    // ---------- Ticking and rendering ----------

    @Test
    void tickComponents_callsTickOnAllComponentsInContainer() {
        SimpleUIComponentContainer container = new SimpleUIComponentContainer();
        TestGUIInputDistributor distributor = new TestGUIInputDistributor();

        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        TestComponent c1 = new TestComponent();
        TestComponent c2 = new TestComponent();

        manager.addComponent(c1);
        manager.addComponent(c2);

        manager.tickComponents();

        assertTrue(c1.tickCalled);
        assertTrue(c2.tickCalled);
    }

    @Test
    void renderComponents_callsRenderOnAllComponentsInContainer() {
        SimpleUIComponentContainer container = new SimpleUIComponentContainer();
        TestGUIInputDistributor distributor = new TestGUIInputDistributor();

        UIComponentManager manager =
                UIComponentManager.buildUIComponentManager(container, distributor);

        TestComponent c1 = new TestComponent();
        TestComponent c2 = new TestComponent();

        manager.addComponent(c1);
        manager.addComponent(c2);

        manager.renderComponents(null);

        assertTrue(c1.renderCalled);
        assertTrue(c2.renderCalled);
    }

    // ---------- Helper test doubles for input ----------

    private static class TestBoundedMouseInputListener implements BoundedMouseInputListener {
        @Override
        public boolean withinBounds(Coord c) {
            return true;
        }

        @Override public void onMouseMoved(MouseInputEvent e) {}
        @Override public void onMouseDragged(MouseInputEvent e) {}
        @Override public void onMousePressed(MouseInputEvent e) {}
        @Override public void onMouseReleased(MouseInputEvent e) {}
        @Override public void onMouseClicked(MouseInputEvent e) {}
        @Override public void onMouseWheel(MouseInputEvent e) {}
    }

    private static class TestKeyListener implements KeyListener {
        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

}