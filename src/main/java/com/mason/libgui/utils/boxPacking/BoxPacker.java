package com.mason.libgui.utils.boxPacking;

import com.mason.libgui.core.UIComponent;

public interface BoxPacker{

    <T extends UIComponent> void pack(int x, int y, int width, int height, T[] components);

}
