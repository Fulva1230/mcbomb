package com.gmail.noxdawn

import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class DropsCollectorImpl(private val plugin: JavaPlugin) : DropsCollector {
    override fun getDrops(): Iterable<Item> {
        return sequence {
            for (world in plugin.server.worlds) {
                for (chunk in world.loadedChunks) {
                    for (entity in chunk.entities) {
                        if (entity is Item) {
                            yield(entity)
                        }
                    }
                }
            }
        }.asIterable()
    }
}
