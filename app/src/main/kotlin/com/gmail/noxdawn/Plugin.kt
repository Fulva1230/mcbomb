package com.gmail.noxdawn

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class Plugin : JavaPlugin() {
    lateinit var koinApp: KoinApplication

    override fun onEnable() {
        logger.info("on enable")
        val plugin_module = module {
            single { this@Plugin } bind JavaPlugin::class
        }

        koinApp = startKoin {
            modules(plugin_module)
            modules(module)
        }

        koinApp.koin.getAll<CommandSpec>().forEach {
            koinApp.koin.get<Logger>().debug("register command executor $it")
            getCommand(it.name)?.setExecutor(it.executor)
        }
        koinApp.koin.getAll<Listener>().forEach {
            koinApp.koin.get<Logger>().debug("register $it")
            server.pluginManager.registerEvents(it, this)
        }
    }

    override fun onDisable() {
        logger.info("on disable")
    }
}
