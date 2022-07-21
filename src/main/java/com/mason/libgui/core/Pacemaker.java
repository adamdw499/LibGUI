
package com.mason.libgui.core;

import static com.mason.libgui.utils.Utils.PERFORMANCE_LOG;
import static java.lang.System.currentTimeMillis;

/**
 *
 * @author Adam Whittaker
 */
public class Pacemaker implements Runnable{

    
    private double tickRate = 1/60D;
    private boolean running = true;
    private int fps = 0, tps = 0;
    private final GUIManager gui;
    
    
    public Pacemaker(GUIManager gm){
        gui = gm;
    }
    

    @Override
    public void run(){
        double tickDelta = 0;
        long currentTime, lastTick = currentTimeMillis(), statTime = lastTick + 1000;
        
        while(running){
            currentTime = currentTimeMillis();
            tickDelta += (currentTime - lastTick) / 1000D;
            
            while(tickDelta > tickRate){
                tick();
                tickDelta -= tickRate;
            }
            lastTick = currentTimeMillis();
            
            render();
            
            if(currentTime > statTime){
                PERFORMANCE_LOG.printStats(fps, tps);
                fps = 0;
                tps = 0;
                statTime += 1000;
            }
        }
        PERFORMANCE_LOG.println("Pacemaker terminated");
    }
    
    private void tick(){
        tps++;
        gui.tick();
    }
    
    private void render(){
        fps++;
        gui.render();
    }
    
    public void terminate(){
        running = false;
        PERFORMANCE_LOG.dualPrintln("Program Terminated");
    }

    public void setTickRate(double _tickRate){
        tickRate = _tickRate;
    }
    
}
