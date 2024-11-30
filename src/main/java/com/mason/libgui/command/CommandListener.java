package com.mason.libgui.command;

public interface CommandListener{

    boolean acceptsCommand(Command command);

    void executeCommand(Command command);

}
