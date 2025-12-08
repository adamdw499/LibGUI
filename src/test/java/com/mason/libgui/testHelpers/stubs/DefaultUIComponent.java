package com.mason.libgui.testHelpers.stubs;

import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.component.UIComponent;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.InputDelegator;
import com.mason.libgui.core.input.mouse.MouseInputBounder;
import com.mason.libgui.utils.structures.Boundable;
import com.mason.libgui.utils.structures.Coord;
import com.mason.libgui.utils.structures.Size;

import java.awt.*;

public class DefaultUIComponent extends UIComponent implements InputDelegator{


    private Color color;
    private Runnable ticker;
    private final Size size;
    protected final BoundedMouseInputListener mouseInput;


    public DefaultUIComponent(Coord topLeft, Size size, Color color, Runnable ticker, MouseInputBounder mouseInput){
        super(new HitboxRect(topLeft, size));
        this.color = color;
        this.ticker = ticker;
        this.size = size;
        this.mouseInput = mouseInput.fromBounds(this);
    }

    public DefaultUIComponent(Coord topLeft, Size size, MouseInputBounder mouseInput){
        this(topLeft, size, new Color(200, 180, 170), () -> {}, mouseInput);
    }


    public void setColor(Color color){
        this.color = color;
    }

    public void setTicker(Runnable ticker){
        this.ticker = ticker;
    }


    @Override
    public void render(Graphics2D g){
        g.setColor(color);
        g.fillRect(getCoord().x(), getCoord().y(), size.width(), size.height());
    }

    @Override
    public void tick(){
        ticker.run();
    }

    @Override
    public void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        mouseInput.setInputSource(inputSource);
    }

    @Override
    public void unsetInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        mouseInput.unsetInputSource(inputSource);
    }

}
