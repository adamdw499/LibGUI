package com.mason.libgui.core.input.componentLayer;

import java.util.*;

public class OrderedListenerRegister<T> implements ListenerRegister<T>{


    private final Set<T> listeners;
    private final Deque<T> order;


    public OrderedListenerRegister(){
        listeners = new HashSet<>();
        order = new ArrayDeque<>();
    }


    @Override
    public void addListener(T listener){
        if(!listeners.contains(listener)){
            listeners.add(listener);
            order.add(listener);
        }
    }

    @Override
    public void removeListener(T listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
            order.remove(listener);
        }
    }

    @Override
    public Iterator<T> iterator(){
        return order.descendingIterator();
    }

}
