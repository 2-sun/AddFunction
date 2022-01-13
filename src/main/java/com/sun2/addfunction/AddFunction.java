package com.sun2.addfunction;

import org.bukkit.plugin.java.JavaPlugin;

public final class AddFunction extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("af").setExecutor(new AFCommand(this));
        getServer().getPluginManager().registerEvents(new AFListener(this), this);
        saveConfig();
        getLogger().info("plugin is ready");
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}