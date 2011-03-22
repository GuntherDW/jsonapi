package com.alecgorge.bukkit.jsonapi;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;

import java.util.logging.Logger;

/**
 * @author GuntherDW
 */
public class JSONConsoleCommandSender extends ConsoleCommandSender {
    private Boolean op = false;
    private String ConsoleCMD = null;
    private final Logger log = Logger.getLogger("Minecraft");


    /**
     *
     * @param server  - Server
     * @param consoleCMD - RelayedMessage
     * @param isOp - Boolean
     */
    public JSONConsoleCommandSender(Server server, String consoleCMD, Boolean isOp) {
        super(server);
        this.ConsoleCMD = consoleCMD;
        this.op = isOp;
    }

    public boolean isOp() { return this.op; }

    @Override
    public boolean isPlayer() { return false; }

    @Override
    public void sendMessage(String message) {
        log.info("JSONConsoleCommand sent "+message+"!");
    }
}
