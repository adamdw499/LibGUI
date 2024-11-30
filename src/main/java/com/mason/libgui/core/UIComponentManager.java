
package com.mason.libgui.core;

import com.mason.libgui.components.dragging.DragManager;
import com.mason.libgui.components.dragging.Draggable;
import com.mason.libgui.components.misc.ClickBlocker;
import com.mason.libgui.components.misc.ClickOffable;
import com.mason.libgui.components.panes.Pane;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner;
import com.mason.libgui.utils.UIAligner.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;

import static com.mason.libgui.utils.Utils.withinRectBounds;


/**
 *
 * @author Adam Whittaker
 */
public class UIComponentManager extends UIComponent{
    
    
    private final List<UIComponent> components = new LinkedList<>();
    private final SpawningManager spawner = new SpawningManager(components);
    protected int mouseX, mouseY;
    protected StyleInfo info;
    protected final DragManager dragManager;
    private Pane dragPane;
    
    private UIAligner aligner = UIAligner.DEFAULT_ALIGNER;
    
    
    public UIComponentManager(StyleInfo info, int w, int h){
        this(info, 0, 0, w, h);
    }
    
    protected UIComponentManager(StyleInfo info, int x, int y, int w, int h){
        this(new DragManager(), info, x, y, w, h);
    }

    protected UIComponentManager(DragManager manager, StyleInfo info, int x, int y, int w, int h){
        super(x, y, w, h);
        dragManager = manager;
        this.info = info;
    }
    
    
    public void setAligner(UIAligner a){
        aligner = a;
    }
    
    
    public void blockClicks(){
        components.add(0, new ClickBlocker(0, 0, width, height));
    }
    
    public void unblockClicks(){
        int n = 0;
        for(;n < components.size(); n++) if(components.get(n) instanceof ClickBlocker) break;
        components.remove(n);
    }

    public DragManager getDragManager(){
        return dragManager;
    }

    protected UIComponent getComponent(int n){
        return components.get(n);
    }

    public int componentNum(){
        return components.size();
    }

    public void addComponent(UIComponent comp){
        comp.setParent(this);
        spawner.addComponent(comp);
    }

    protected void moveComponentToFront(UIComponent comp){
        removeComponent(comp);
        addComponent(comp);
    }

    public void addComponent(UIComponent comp, Position hor, Position vert){
        aligner.align(comp, width, height, hor, vert);
        addComponent(comp);
    }
    
    public void removeComponent(UIComponent comp){
        spawner.removeComponent(comp);
    }

    public void addToBackground(UIComponent comp){
        spawner.addToBackground(comp);
    }
    
    @Override
    public void tick(int mx, int my){
        components.forEach(c -> c.tick(mouseX, mouseY));
        spawner.tick();
    }
    
    @Override
    public void render(Graphics2D g){
        for(int n = components.size()-1; n>=0; n--){
            components.get(n).render(g);
        }
    }

    @Override
    public boolean withinBounds(int mx, int my){
        return withinRectBounds(x, y, width, height, mx, my);
    }

    
    @Override
    public void mouseClicked(MouseEvent e){
        for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseClicked(e);
                break;
            }
        }
        components.stream().filter(s -> s instanceof ClickOffable && !s.withinBounds(e.getX(), e.getY()))
                .forEach(s -> ((ClickOffable) s).clickOff());
    }

    @Override
    public void mousePressed(MouseEvent e){
        for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                if(comp instanceof Draggable drag &&
                        drag.withinDragRegion(e.getX() - drag.getX(), e.getY() - drag.getY())){
                    dragManager.startDrag(drag, e.getX(), e.getY());
                }else{
                    comp.mousePressed(e);
                }
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if(dragManager.isDragging()){
            /*if(isPaneDragging()){
                dragPane.mouseReleased(e);
                dragPane = null;
            }else{
                dragging.mouseReleased(e);
                dragging = null;
            }*/
            dragManager.releaseDrag();
        }else for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseReleased(e);
                break;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseWheelMoved(e);
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(dragManager.isDragging()){
            dragManager.mouseDragged(e);
        }else for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseDragged(e);
                break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
        mouseX = e.getX();
        mouseY = e.getY();
        for(UIComponent comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseMoved(e);
                break;
            }
        }
    }
    
}
