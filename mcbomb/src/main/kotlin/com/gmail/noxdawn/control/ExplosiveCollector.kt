package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class Explosive(
    private val item: Item,
    private val triggerTagger: Tagger<Int>,
    private val powerTagger: Tagger<Double>,
) : Trigger {
    override val isTriggered: Boolean
        get() = triggerTagger.value == 1

    override fun trigger() {
        item.world.createExplosion(item.location, powerTagger.value?.toFloat() ?: 3.0f)
        item.remove()
    }

}

class ExplosiveCollector(
    private val plugin: JavaPlugin,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val powerTaggerBuilder: Tagger.Builder<Double>,
) : TriggerCollector {
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

    override fun getTriggers(): Iterable<Trigger> = getDropItems().mapNotNull { item ->
        val triggerTagger = triggerTaggerBuilder.getTagger(item)
        val powerTagger = powerTaggerBuilder.getTagger(item)
        if (triggerTagger.hasValue() && powerTagger.hasValue()) {
            return@mapNotNull Explosive(item, triggerTagger, powerTagger)
        }
        null
    }.asIterable()
}
