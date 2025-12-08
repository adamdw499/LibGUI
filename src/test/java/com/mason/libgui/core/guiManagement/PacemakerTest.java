package com.mason.libgui.core.guiManagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mason.libgui.utils.structures.*;

import java.util.concurrent.atomic.AtomicInteger;

class PacemakerTest{


    @Test
    void tickAndRenderAreInvokedWhileRunning() throws Exception {
        AtomicInteger tickCount = new AtomicInteger();
        AtomicInteger renderCount = new AtomicInteger();

        Pacemaker pacemaker = new Pacemaker(
                renderCount::incrementAndGet,
                tickCount::incrementAndGet
        );

        Thread thread = new Thread(pacemaker);
        thread.start();

        // Let it run a bit
        Thread.sleep(100);

        pacemaker.terminate();
        thread.join(1000);

        assertFalse(thread.isAlive(), "Pacemaker thread should have terminated");
        assertTrue(renderCount.get() > 0, "renderAll should have been called at least once");
        assertTrue(tickCount.get() > 0, "tickAll should have been called at least once");
    }

    @Test
    void terminateStopsRunLoopPromptly() throws Exception {
        AtomicInteger tickCount = new AtomicInteger();
        AtomicInteger renderCount = new AtomicInteger();

        Pacemaker pacemaker = new Pacemaker(
                renderCount::incrementAndGet,
                tickCount::incrementAndGet
        );

        Thread thread = new Thread(pacemaker);
        thread.start();

        // Terminate almost immediately
        Thread.sleep(50);
        pacemaker.terminate();

        thread.join(1000);

        assertFalse(thread.isAlive(), "Pacemaker thread should have stopped after terminate()");
    }

    @Test
    void setTickRateAdjustsTickDelay() throws Exception {
        AtomicInteger dummy = new AtomicInteger();
        Pacemaker pacemaker = new Pacemaker(dummy::incrementAndGet, dummy::incrementAndGet);

        // Use reflection to inspect private tickDelay
        var field = Pacemaker.class.getDeclaredField("tickDelay");
        field.setAccessible(true);

        pacemaker.setTickRate(20); // 20 ticks per second -> 50 ms delay
        int delay20 = field.getInt(pacemaker);
        assertEquals(50, delay20);

        pacemaker.setTickRate(100); // 100 tps -> 10 ms delay
        int delay100 = field.getInt(pacemaker);
        assertEquals(10, delay100);
    }

    @Test
    void higherTickRateProducesMoreTicksOverSameDuration() throws Exception {
        // Low tick rate
        AtomicInteger lowTickCount = new AtomicInteger();
        Pacemaker lowRate = new Pacemaker(
                () -> {},           // ignore renders
                lowTickCount::incrementAndGet
        );
        lowRate.setTickRate(10);     // 10 tps

        Thread lowThread = new Thread(lowRate);
        lowThread.start();
        Thread.sleep(100);
        lowRate.terminate();
        lowThread.join(1000);

        int lowTicks = lowTickCount.get();

        // High tick rate
        AtomicInteger highTickCount = new AtomicInteger();
        Pacemaker highRate = new Pacemaker(
                () -> {},
                highTickCount::incrementAndGet
        );
        highRate.setTickRate(100);   // 100 tps

        Thread highThread = new Thread(highRate);
        highThread.start();
        Thread.sleep(100);
        highRate.terminate();
        highThread.join(1000);

        int highTicks = highTickCount.get();

        // High tick rate should give noticeably more ticks
        assertTrue(highTicks > lowTicks, "Higher tick rate should produce more ticks");
    }

}