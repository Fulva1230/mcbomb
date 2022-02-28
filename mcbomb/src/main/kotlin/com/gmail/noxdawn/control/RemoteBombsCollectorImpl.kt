package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class RemoteBombsCollectorImpl(
    private val plugin: JavaPlugin,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val remoteBombLabelTaggerBuilder: Tagger.Builder<String>
) : RemoteBombsCollector {
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

    override fun getRemoteBombs(): Iterable<RemoteBomb> =
        getDropItems().mapNotNull { itemDrop ->
            val triggerTagger = triggerTaggerBuilder.getTagger(itemDrop)
            val remoteBombLabelTagger = remoteBombLabelTaggerBuilder.getTagger(itemDrop)
            if (triggerTagger.hasValue() && remoteBombLabelTagger.hasValue()) {
                return@mapNotNull RemoteBombImpl(remoteBombLabelTagger, triggerTagger)
            }
            null
        }.asIterable()
}
