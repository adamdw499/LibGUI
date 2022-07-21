package com.mason.libgui.utils.exceptionHandlers;

import com.mason.libgui.utils.ExceptionHandler;

import static com.mason.libgui.utils.Utils.PERFORMANCE_LOG;

public class FreezeExceptionHandler extends ExceptionHandler{


    @Override
    public void handleException(Exception e){
        e.printStackTrace(System.err);
        PERFORMANCE_LOG.log(e);
        PERFORMANCE_LOG.dualPrintln("Program Frozen");
        pacemaker.setTickRate(Double.MAX_VALUE);
    }

}
