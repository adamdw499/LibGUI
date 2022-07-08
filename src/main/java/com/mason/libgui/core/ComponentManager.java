
package com.mason.libgui.core;

import com.mason.libgui.components.ClickBlocker;
import com.mason.libgui.components.Draggable;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Adam Whittaker
 */
public class ComponentManager extends Component{
    
    
    protected final List<Component> components = new LinkedList<>();
    private final List<Component> spawning = new LinkedList<>(), despawning = new LinkedList<>();
    protected int mouseX, mouseY;
    private Draggable dragging;
    
    
    public ComponentManager(int w, int h){
        super(0, 0, w, h);
    }
    
    protected ComponentManager(int x, int y, int w, int h){
        super(x, y, w, h);
    }
    
    
    public void blockClicks(){
        components.add(0, new ClickBlocker(0, 0, width, height));
    }
    
    public void unblockClicks(){
        int n = 0;
        for(;n < components.size(); n++) if(components.get(n) instanceof ClickBlocker) break;
        components.remove(n);
    }
    
    public void addComponent(Component comp){
        spawning.add(comp);
    }
    
    public void removeComponent(Component comp){
        despawning.add(comp);
    }
    
    @Override
    public void tick(int mx, int my){
        components.forEach(c -> c.tick(mouseX, mouseY));
        spawning.forEach(comp -> {
            components.add(0, comp);
        });
        if(!despawning.isEmpty()) components.removeAll(despawning);
        spawning.clear();
        despawning.clear();
    }
    
    @Override
    public void render(Graphics2D g){
        for(int n = components.size()-1; n>=0; n--){
            components.get(n).render(g);
        }
    }

    
    @Override
    public void mouseClicked(MouseEvent e){
        for(Component comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseClicked(e);
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){
        for(Component comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                if(comp instanceof Draggable draggable) dragging = draggable;
                comp.mousePressed(e);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if(dragging != null){
            dragging.mouseReleased(e);
            dragging = null;
        }else for(Component comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseReleased(e);
                break;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        for(Component comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseWheelMoved(e);
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(dragging != null){
            dragging.mouseDragged(e);
        }else for(Component comp : components){
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
        for(Component comp : components){
            if(comp.withinBounds(e.getX(), e.getY())){
                comp.mouseMoved(e);
                break;
            }
        }
    }
    
}
