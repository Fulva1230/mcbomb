package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class BombCollectorImpl(
    private val plugin: JavaPlugin,
    private val countDownTaggerBuilder: Tagger.Builder<Int>,
    private val powerTaggerBuilder: Tagger.Builder<Double>,
) : BombCollector {
    override fun getBombs(): Iterable<BombState> {
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
        }.map { item ->
            val itemMetaCopy = item.itemStack.itemMeta
            if (itemMetaCopy != null) {
                val countDownTagger = countDownTaggerBuilder.getTagger(itemMetaCopy)
                if (countDownTagger.hasValue()) {
                    val powerTagger = powerTaggerBuilder.getTagger(itemMetaCopy)
                    return@map BombState(
                        id = item.uniqueId,
                        count = countDownTagger.value,
                        shouldExplode = false,
                        power = powerTagger.value
                    )
                }
            }
            null
        }.filterNotNull().asIterable()
    }
}
