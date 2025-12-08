package com.mason.libgui.core.input.componentLayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class OrderedListenerRegisterTest {

    @Test
    void newRegister_hasNoListenersAndEmptyIterator() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        Iterator<String> it = register.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void addListener_addsListenerOnce() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        assertEquals(1, seen.size());
        assertEquals("A", seen.get(0));
    }

    @Test
    void addListener_doesNotAddDuplicates() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");
        register.addListener("A");
        register.addListener("A");

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        assertEquals(1, seen.size(), "Listener should only appear once");
        assertEquals("A", seen.get(0));
    }

    @Test
    void iterator_returnsListenersInReverseOrderOfAddition() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");
        register.addListener("B");
        register.addListener("C");

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        // order is a deque where we add at tail, iterator() uses descendingIterator()
        // so we expect: last added first
        assertEquals(List.of("C", "B", "A"), seen);
    }

    @Test
    void removeListener_removesFromBothSetAndOrder() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");
        register.addListener("B");
        register.addListener("C");

        register.removeListener("B");

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        assertEquals(List.of("C", "A"), seen);
        assertFalse(seen.contains("B"));
    }

    @Test
    void removeListener_nonExistentListener_doesNothing() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");
        register.addListener("B");

        register.removeListener("X"); // not present

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        // order remains unchanged (descending: B, A)
        assertEquals(List.of("B", "A"), seen);
    }

    @Test
    void addAndRemoveSequence_maintainsCorrectReverseOrder() {
        OrderedListenerRegister<String> register = new OrderedListenerRegister<>();

        register.addListener("A");
        register.addListener("B");
        register.addListener("C");
        register.removeListener("B");
        register.addListener("D");   // D added after C

        List<String> seen = new ArrayList<>();
        register.iterator().forEachRemaining(seen::add);

        // added: A, B (removed), C, D
        // present: A, C, D
        // descending order: D, C, A
        assertEquals(List.of("D", "C", "A"), seen);
    }

}