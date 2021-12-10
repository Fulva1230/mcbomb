package com.gmail.noxdawn

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class EventListener(private val plugin: JavaPlugin, private val integerTaggerBuilder: IntegerTagger.Builder) :
    Listener {
    @EventHandler
    fun onItemPickup(event: EntityPickupItemEvent) {
        val tagger = integerTaggerBuilder.getTagger(event.item)
        plugin.logger.log(Level.FINE, "If the item has tag : ${tagger.hasValue()}")
    }
}
