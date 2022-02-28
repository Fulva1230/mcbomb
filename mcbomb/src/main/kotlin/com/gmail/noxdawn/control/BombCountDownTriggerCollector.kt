package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class BombCountDownTriggerCollector(
    private val plugin: JavaPlugin,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val countTaggerBuilder: Tagger.Builder<Int>,
) : CountDownTimerCollector {
    private fun getDropItems(): Sequence<Item> = sequence {
        for (world in plugin.server.worlds) {
            for (chunk in world.loadedChunks) {
                for (entity in chunk.entities) {
                    if (entity is Item) {
                        yield(entity)
                    }
                }
            }
        }
    }

    override fun getTimers(): Iterable<CountDownTimer> = getDropItems().mapNotNull { item ->
        val triggerTagger = triggerTaggerBuilder.getTagger(item)
        val countTagger = countTaggerBuilder.getTagger(item)
        if (triggerTagger.hasValue() && countTagger.hasValue()) {
            return@mapNotNull BombCountDownTrigger(item, countTagger, triggerTagger)
        }
        null
    }.asIterable()
}
