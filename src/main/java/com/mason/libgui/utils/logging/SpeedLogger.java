package com.mason.libgui.utils.logging;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static com.mason.libgui.utils.logging.ActivityLogger.ACTIVITY_LOGGER;

public final class SpeedLogger{


    public static final SpeedLogger SPEED_LOGGER = new SpeedLogger();


    private long now = System.currentTimeMillis();
    private final List<Entry<Long, String>> records = new LinkedList<>();
    private String currentMessage;


    private SpeedLogger(){}


    public void start(){
        refreshNow();
    }

    public void refreshNow(){
        now = System.currentTimeMillis();
    }

    public void logTime(String message){
        long time = System.currentTimeMillis() - now;
        recordTime(time, message);
        setTimeMessage(time, message);
        ACTIVITY_LOGGER.println(currentMessage);
        refreshNow();
    }

    private void recordTime(long time, String message){
        records.add(new SimpleEntry<>(time, message));
    }

    private void setTimeMessage(long time, String message){
        currentMessage = time + " millis: " + message;
    }

    public String getCurrentMessage(){
        return currentMessage;
    }


    public void logReport(){
        long totalTimeTaken = getTotalTimeTaken();
        ACTIVITY_LOGGER.println("     ---- Speed Report ----");
        ACTIVITY_LOGGER.println("Total time: " + totalTimeTaken + " millis");
        logRecords(totalTimeTaken);
    }

    private long getTotalTimeTaken(){
        return records.stream()
                .mapToLong(Entry::getKey)
                .sum();
    }

    private void logRecords(long totalTimeTaken){
        for(Entry<Long, String> e : records){
            String percentage = computePercentageString(e.getKey(), totalTimeTaken);
            ACTIVITY_LOGGER.println(e.getValue() + ": " + e.getKey() + "ms (" + percentage + "%)");
        }
    }

    private String computePercentageString(long timeTaken, long totalTimeTaken){
        int PRECISION = 4; //Maximum 16 which is precision of doubles.
        double percentageFraction = (100D * timeTaken)/totalTimeTaken;
        String percentage = "" + percentageFraction;
        if(percentage.length() < PRECISION){
            percentage += "00000000000000000";
        }
        percentage = percentage.substring(0, PRECISION);
        return percentage;
    }

}
