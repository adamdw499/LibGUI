package com.mason.libgui.utils.exceptionHandlers;

import com.mason.libgui.utils.ExceptionHandler;

import static com.mason.libgui.utils.Utils.PERFORMANCE_LOG;

public class FailExceptionHandler extends ExceptionHandler{


    @Override
    public void handleException(Exception e){
        e.printStackTrace(System.err);
        PERFORMANCE_LOG.log(e);
        pacemaker.terminate();
    }

}
