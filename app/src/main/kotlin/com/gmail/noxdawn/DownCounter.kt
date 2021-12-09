package com.gmail.noxdawn

import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

interface DropsCollector {
    fun getDrops(): List<Item>
}

interface ItemTagger {
    var count: Int

    fun getTagger(item: Item): ItemTagger
}

class DownCounter(
    private val dropsCollector: DropsCollector,
    private val itemTagger: ItemTagger
) {
    val task
        get() = Runnable()

    private fun activate() {
        for (item in dropsCollector.getDrops()) {
            val tagger = itemTagger.getTagger(item)
            if (tagger.count > 0)
                --tagger.count
        }
    }

    inner class Runnable : BukkitRunnable() {
        override fun run() {
            activate()
        }
    }
}
