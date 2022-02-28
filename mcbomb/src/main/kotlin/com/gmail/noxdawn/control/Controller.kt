package com.gmail.noxdawn.control

import com.gmail.noxdawn.TaskRegistry
import com.gmail.noxdawn.TaskSpec
import com.gmail.noxdawn.TaskType
import org.bukkit.scheduler.BukkitRunnable

interface Controller {
    val period: Int
    fun activate()
}

class Runner(
    private val taskRegistry: TaskRegistry, val controllers: List<Controller>
) {
    private var tick = 0

    private fun activate() {
        ++tick
        for (controller in controllers) {
            if (tick % controller.period == 0) {
                controller.activate()
            }
        }
    }

    init {
        taskRegistry.registerTask(TaskSpec(0, 1, object : BukkitRunnable() {
            override fun run() {
                activate()
            }
        }, TaskType.PERIODIC))
    }
}
