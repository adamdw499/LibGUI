
package com.mason.libgui.utils.logging;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Coord;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class ActivityLogger extends PrintStream implements KeyListener{
    

    public static final ActivityLogger ACTIVITY_LOGGER;
    static{
        try{
            ACTIVITY_LOGGER = new ActivityLogger();
        }catch(FileNotFoundException ex){
            throw new RuntimeException(ex);
        }
    }


    private final DateFormat DATE_FORMAT = new
        SimpleDateFormat("MM-dd HH:mm:ss:SSS: ");

    private final boolean FILE_CRASH_REPORT = true;
    private final boolean PRINT_CLICKS = true;
    private final boolean PRINT_KEYS = true;
    private final boolean PRINT_KEY_HOLDS = true;
    private final boolean PRINT_FRAME_AND_TICK_RATE = true;
    private static final String FILE_NAME = "log/performance.txt";


    private ActivityLogger() throws FileNotFoundException{
        super(new File(FILE_NAME));
        printFreeMemory();
    }


    public void printFreeMemory(){
        println("Free memory: " + getFreeMemoryInMB() + "MB");
    }

    private long getFreeMemoryInMB(){
        return Runtime.getRuntime().freeMemory()/1048576;
    }

    @Override
    public void println(String str){
        super.println(DATE_FORMAT.format(new Date()) + str);
    }

    public void logException(Exception exception){
        writeCrashReportToPrintStream(exception, this);
        fileCrashReport(exception);
    }

    private void writeCrashReportToPrintStream(Exception exception, PrintStream stream){
        stream.println("$ " + exception.getClass().getName() +
                ": " + exception.getMessage());
        exception.printStackTrace(stream);
    }

    public void printFrameAndTickRate(int fps, int tps){
        if(PRINT_FRAME_AND_TICK_RATE){
            println("FPS: " + fps + ", TPS: " + tps);
        }
    }

    public void fileCrashReport(Exception exception){
        if(FILE_CRASH_REPORT){
            generateCrashReportFile(exception);
        }
    }

    private void generateCrashReportFile(Exception exception){
        String crashFileName = getCrashFileName();
        try(PrintStream stream = new PrintStream(crashFileName)){
            writeCrashReportToPrintStream(exception, stream);
        }catch(IOException ex){
            System.err.println("Failed to create PrintStream with name: " + crashFileName);
            ex.printStackTrace(System.err);
        }
    }

    private String getCrashFileName(){
        String logPath = "log";
        Set<String> fileNames = getSetOfExistingFileNamesInDirectory(logPath);

        String crashFileName = generateUniqueCrashFilename(fileNames);
        return logPath + "/" + crashFileName;
    }

    private Set<String> getSetOfExistingFileNamesInDirectory(String filepath){
        Set<String> fileNames = new HashSet<>();
        File[] files = getExistingFilesInDirectory(filepath);
        for(File entry : files){
            fileNames.add(entry.getName());
        }
        return fileNames;
    }

    private File[] getExistingFilesInDirectory(String filepath){
        File[] files = new File(filepath).listFiles();
        if(files == null){
            throw new NullPointerException("No file with pathname: " + filepath);
        }
        return files;
    }

    private String generateUniqueCrashFilename(Set<String> existingNames){
        int n = 0;
        String name = "crash0.txt";
        while(existingNames.contains(name)){
            n++;
            name = "crash" + n + ".txt";
        }
        return name;
    }


    public void printMouseInputEvent(MouseInputEvent e){
        if(PRINT_CLICKS){
            Coord coord = e.getCoord();
            printf("Mouse %s (%d, %d)%n", e.getType(), coord.x(), coord.y());
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        if(PRINT_KEYS){
            println("Key typed: " + e.paramString());
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(PRINT_KEY_HOLDS){
            println("Key pressed: " + e.paramString());
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        if(PRINT_KEY_HOLDS){
            println("Key released: " + e.paramString());
        }
    }
    
}
