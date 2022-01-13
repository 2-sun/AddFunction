package com.sun2.addfunction;

import org.bukkit.plugin.java.JavaPlugin;

public final class AddFunction extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("af").setExecutor(new AFCommand(this));
        saveConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
// -636 73 -337
