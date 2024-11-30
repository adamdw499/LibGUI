package com.mason.libgui.command;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CommandInput implements Runnable{


    private final List<CommandListener> listeners = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Function<String, Command> parser;
    private volatile boolean running = false;


    public CommandInput(Function<String, Command> parser){
        this.parser = parser;
    }


    public void addListener(CommandListener listener){
        listeners.add(listener);
    }

    public void removeListener(CommandListener listener){
        listeners.remove(listener);
    }

    private void distributeCommand(Command command){
        if(command == null){
            System.out.println("Malformed Command");
            return;
        }
        if(command instanceof SystemCommand sysCom){
            sysCom.run();
            return;
        }

        for(CommandListener listener : listeners) if(listener.acceptsCommand(command)){
            listener.executeCommand(command);
        }
    }

    public boolean isRunning(){
        return running;
    }

    public void stop(){
        running = false;
    }

    public void start(){
        running = true;
        new Thread(this, "CommandInput Thread").start();
        System.out.println("Command input stopped");
    }

    @Override
    public void run(){
        while(running){
            distributeCommand(parser.apply(scanner.nextLine()));
        }
    }

}
