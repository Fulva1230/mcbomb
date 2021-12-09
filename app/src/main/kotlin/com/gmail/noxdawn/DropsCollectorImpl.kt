package com.gmail.noxdawn

import org.bukkit.World
import org.bukkit.entity.Item

class DropsCollectorImpl(private val world: World) : DropsCollector {
    override fun getDrops(): List<Item> {
        val returnList = mutableListOf<Item>()
        for (chunk in world.loadedChunks) {
            for (entity in chunk.entities) {
                if (entity is Item) {
                    returnList.add(entity)
                }
            }
        }
        return returnList.toList()
    }
}
