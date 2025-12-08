
package com.mason.libgui.core.guiManagement;

import static com.mason.libgui.utils.logging.ActivityLogger.ACTIVITY_LOGGER;
import static java.lang.System.currentTimeMillis;

public class Pacemaker implements Runnable{

    
    private int tickDelay = 1000/60;
    private boolean running = true;
    private int framesPerSecond = 0;
    private int ticksPerSecond = 0;
    private long currentTime;
    private long timeOfLastTick;
    private long timeSinceLastTick;
    private long statTime;
    private final Runnable renderAll;
    private final Runnable tickAll;


    Pacemaker(Runnable renderAll, Runnable tickAll){
        this.renderAll = renderAll;
        this.tickAll = tickAll;
    }


    @Override
    public void run(){
        timeOfLastTick = currentTimeMillis();
        statTime = timeOfLastTick + 1000;
        while(running){
            clockCycle();
        }
        ACTIVITY_LOGGER.println("Pacemaker finished");
    }

    private void clockCycle(){
        currentTime = currentTimeMillis();
        timeSinceLastTick += currentTime - timeOfLastTick;
        performAnyMissedTicks();
        render();
        resetAndLogStatsIfSecondHasPassed();
    }

    private void performAnyMissedTicks(){
        while(timeSinceLastTick > tickDelay){
            tick();
            timeSinceLastTick -= tickDelay;
        }
        timeOfLastTick = currentTimeMillis();
    }
    
    private void tick(){
        ticksPerSecond++;
        tickAll.run();
    }
    
    private void render(){
        framesPerSecond++;
        renderAll.run();
    }

    private void resetAndLogStatsIfSecondHasPassed(){
        if(currentTime > statTime){
            ACTIVITY_LOGGER.printFrameAndTickRate(framesPerSecond, ticksPerSecond);
            framesPerSecond = 0;
            ticksPerSecond = 0;
            statTime += 1000;
        }
    }


    void terminate(){
        running = false;
        ACTIVITY_LOGGER.println("Program Terminated");
    }

    void setTickRate(int ticksPerSecond){
        tickDelay = 1000/ticksPerSecond;
    }
    
}
