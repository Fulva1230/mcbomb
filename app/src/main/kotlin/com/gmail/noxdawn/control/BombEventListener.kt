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
    private val bombCountTaggerBuilder: Tagger.BuilderForItems<Int>,
    private val bombUniqueTaggerBuilder: Tagger.BuilderForItems<UUID>,
    private val logger: Logger
) : Listener {
    @EventHandler
    fun setBombToChainExplosion(event: EntityDamageEvent) {
        val item = event.entity as? Item
        if (item != null) {
            val countTagger = bombCountTaggerBuilder.getTagger(item)
            if (countTagger.hasValue()) {
                countTagger.value = 0
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun itemSpawnEvent(event: ItemSpawnEvent) {
        prepareBomb(event.entity)
    }

    private fun prepareBomb(item: Item) {
        val countTagger = bombCountTaggerBuilder.getTagger(item)
        if (countTagger.hasValue()) {
            item.customName = "${(countTagger.value + 9) / 10}"
            item.isCustomNameVisible = true
            val uniqueTagger = bombUniqueTaggerBuilder.getTagger(item)
            uniqueTagger.value = UUID.randomUUID()
        }
    }
}
