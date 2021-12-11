package com.gmail.noxdawn.control

import com.gmail.noxdawn.Logger
import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ItemSpawnEvent
import java.util.*

class BombEventListener(
    private val bombCountTaggerBuilder: Tagger.Builder<Int>,
    private val bombUniqueTaggerBuilder: Tagger.Builder<UUID>,
    private val logger: Logger
) : Listener {
    @EventHandler
    fun setBombToChainExplosion(event: EntityDamageEvent) {
        val item = event.entity as? Item
        if (item != null) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { dataHolder ->
                val countTagger = bombCountTaggerBuilder.getTagger(dataHolder)
                if (countTagger.hasValue()) {
                    countTagger.value = 0
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun itemSpawnEvent(event: ItemSpawnEvent) {
        prepareBomb(event.entity)
    }

    private fun prepareBomb(item: Item) {
        item.itemStack.itemMeta = item.itemStack.itemMeta?.also {
            val countTagger = bombCountTaggerBuilder.getTagger(it)
            if (countTagger.hasValue()) {
                item.customName = "${(countTagger.value + 9) / 10}"
                item.isCustomNameVisible = true
                val uniqueTagger = bombUniqueTaggerBuilder.getTagger(it)
                uniqueTagger.value = UUID.randomUUID()
            }
        }
    }
}
