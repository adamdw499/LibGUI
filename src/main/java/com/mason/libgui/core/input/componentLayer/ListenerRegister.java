package com.mason.libgui.core.input.componentLayer;

public interface ListenerRegister<T> extends Iterable<T>{

    void addListener(T listener);
    void removeListener(T listener);

}
