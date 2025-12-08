package com.mason.libgui.core.input.componentLayer;

import com.mason.libgui.core.input.mouse.MouseInputListener;
import com.mason.libgui.core.input.guiLayer.GUIInputSocket;

import java.awt.event.*;

public interface GUIInputDistributor<E extends MouseInputListener> extends GUIInputSocket, GUIInputRegister<E>{


}
