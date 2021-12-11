package com.gmail.noxdawn

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import java.util.*

class BombEventListener(
    private val bombCountTaggerBuilder: Tagger.Builder<Int>,
    private val bombUniqueTaggerBuilder: Tagger.Builder<UUID>,
    private val logger: Logger
) : Listener {
    @EventHandler
    fun checkTags(event: EntityPickupItemEvent) {
        val taggerOfItem = bombCountTaggerBuilder.getTagger(event.item)
        logger.debug("If the item has tag: ${taggerOfItem.hasValue()}")
        val taggerOfItemStack = bombCountTaggerBuilder.getTagger(event.item.itemStack.itemMeta!!)
        logger.debug("If the associated itemStack has tag: ${taggerOfItemStack.hasValue()}")
    }

    @EventHandler
    fun bombDropPrepare(event: PlayerDropItemEvent) {
        event.itemDrop.itemStack.itemMeta = event.itemDrop.itemStack.itemMeta?.also {
            val countTagger = bombCountTaggerBuilder.getTagger(it)
            if (countTagger.hasValue()) {
                event.itemDrop.customName = "${(countTagger.value + 9) / 10}"
                event.itemDrop.isCustomNameVisible = true
            }
            val uniqueTagger = bombUniqueTaggerBuilder.getTagger(it)
            uniqueTagger.value = UUID.randomUUID()
        }
    }
}
