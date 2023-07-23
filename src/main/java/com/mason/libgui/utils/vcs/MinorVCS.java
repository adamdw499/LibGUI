package com.mason.libgui.utils.vcs;

import java.util.LinkedList;

public class MinorVCS{


    private String current;
    private final String initial;
    private LinkedList<KeyCommand> history = new LinkedList<>();
    private int currentVersionIndex = -1;


    public MinorVCS(String initial){
        this.initial = initial;
        current = initial;
    }


    public void rollBack(){
        if(hasPast()){
            current = history.get(currentVersionIndex).inverse(current);
            currentVersionIndex--;
        }
    }

    public void rollForward(){
        if(hasFuture()){
            currentVersionIndex++;
            current = history.get(currentVersionIndex).update(current);
        }
    }

    public boolean hasPast(){
        return currentVersionIndex >= 0;
    }

    public boolean hasFuture(){
        return currentVersionIndex < history.size() - 1;
    }

    public void clearFuture(){
        while(hasFuture()) history.remove(currentVersionIndex + 1);
    }

    public void typeChar(char c, int position){
        registerCommand(KeyCommandFactory.generateCharTypeCommand(c, position));
    }

    public void deleteChar(int position){
        registerCommand(KeyCommandFactory.generateDeleteCharCommand(current.charAt(position), position));
    }

    private void registerCommand(KeyCommand command){
        clearFuture();
        currentVersionIndex++;
        history.add(command);
        current = command.update(current);
    }

    public String getText(){
        return current;
    }

    @Override
    public String toString(){
        return "[MinorVCS] " + current;
    }


    public int length(){
        return current.length();
    }

    public String substring(int beginIndex, int endIndex){
        return current.substring(beginIndex, endIndex);
    }

    public String substring(int beginIndex){
        return current.substring(beginIndex);
    }

}
