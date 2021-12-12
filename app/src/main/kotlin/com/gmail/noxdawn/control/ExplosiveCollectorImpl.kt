package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class ExplosiveImpl(
    private val item: Item,
    private val triggerTagger: Tagger<Int>,
    private val powerTagger: Tagger<Double>,
) : Explosive {
    override val isTriggered: Boolean
    get() = triggerTagger.value == 1

    override fun explode() {
        item.world.createExplosion(item.location, powerTagger.value.toFloat())
        item.remove()
    }

}

class ExplosiveCollectorImpl(
    private val plugin: JavaPlugin,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val powerTaggerBuilder: Tagger.Builder<Double>,
) : ExplosiveCollector {
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

    override fun getExplosives(): Iterable<Explosive> =
        getDropItems().mapNotNull { item ->
            val itemMetaCopy = item.itemStack.itemMeta
            if (itemMetaCopy != null) {
                val triggerTagger = triggerTaggerBuilder.getTagger(itemMetaCopy)
                val powerTagger = powerTaggerBuilder.getTagger(itemMetaCopy)
                if (triggerTagger.hasValue() && powerTagger.hasValue()) {
                    return@mapNotNull ExplosiveImpl(item, triggerTagger, powerTagger)
                }
            }
            null
        }.asIterable()
}
