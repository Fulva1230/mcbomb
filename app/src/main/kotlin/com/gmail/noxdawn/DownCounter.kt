package com.gmail.noxdawn

import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

interface DropsCollector {
    fun getDrops(): List<Item>
}

class DownCounter(
    private val dropsCollector: DropsCollector,
    private val itemTaggerBuilder: IntegerTagger.Builder,
    private val taskRegistry: TaskRegistry
) {
    private val task
        get() = TaskSpec(0, 20, Runnable(), TaskType.PERIODIC)

    init {
        taskRegistry.registerTask(task)
    }

    private fun activate() {
        for (item in dropsCollector.getDrops()) {
            val tagger = itemTaggerBuilder.getTagger(item)
            if (tagger.hasValue() && tagger.value > 0) {
                --tagger.value
            }
        }
    }

    inner class Runnable : BukkitRunnable() {
        override fun run() {
            activate()
        }
    }
}
