package com.gmail.noxdawn

import org.bukkit.plugin.java.JavaPlugin

class Plugin : JavaPlugin() {
    override fun onDisable() {
        logger.info("on disable")
    }

    override fun onEnable() {
        logger.info("on enable")
    }
}
