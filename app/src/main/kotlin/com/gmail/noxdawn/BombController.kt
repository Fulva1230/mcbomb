package com.gmail.noxdawn

import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

interface DropsCollector {
    fun getDrops(): Iterable<Item>
}

class BombController(
    private val dropsCollector: DropsCollector,
    private val itemTaggerBuilder: Tagger.Builder<Int>,
    private val taskRegistry: TaskRegistry
) {
    private val task
        get() = TaskSpec(0, 2, Runnable(), TaskType.PERIODIC)

    init {
        taskRegistry.registerTask(task)
    }

    private fun activate() {
        for (item in dropsCollector.getDrops()) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also {
                val tagger = itemTaggerBuilder.getTagger(it)
                if (tagger.hasValue() && tagger.value > 0) {
                    --tagger.value
                    item.customName = "${(tagger.value + 9) / 10}"
                }
            }
        }
    }

    inner class Runnable : BukkitRunnable() {
        override fun run() {
            activate()
        }
    }
}
