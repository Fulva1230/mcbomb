package com.gmail.noxdawn

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

enum class TaskType {
    PERIODIC, ONE_SHOT
}

data class TaskSpec(val delay: Long, val period: Long, val task: BukkitRunnable, val type: TaskType)

interface TaskRegistry {
    fun registerTask(task: TaskSpec)
}

class TaskRegistryImpl(private val plugin: JavaPlugin) : TaskRegistry {
    override fun registerTask(task: TaskSpec) {
        when (task.type) {
            TaskType.PERIODIC -> task.task.runTaskTimer(plugin, task.delay, task.period)
            TaskType.ONE_SHOT -> task.task.runTaskLater(plugin, task.delay)
        }
    }
}
