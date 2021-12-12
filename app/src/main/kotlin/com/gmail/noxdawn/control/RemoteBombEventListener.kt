package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

interface RemoteBomb {
    var label: String
    var trigger: Int
}

interface RemoteBombsCollector {
    fun getRemoteBombs(): Iterable<RemoteBomb>
}

class RemoteBombEventListener(
    private val remoteBombLabelTaggerBuilder: Tagger.Builder<String>,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val remoteBombLabelTaggerBuilderForItems: Tagger.BuilderForItems<String>,
    private val triggerTaggerBuilderForItems: Tagger.BuilderForItems<Int>,
    private val uuidTaggerBuilderForItems: Tagger.BuilderForItems<UUID>,
    private val remoteBombsCollector: RemoteBombsCollector
) : Listener {

    @EventHandler
    fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
        makeBombNonUnique(event.item)
    }

    @EventHandler
    fun onInventoryPickupItemEvent(event: InventoryPickupItemEvent) {
        makeBombNonUnique(event.item)
    }

    fun makeBombNonUnique(item: Item) {
        val remoteBombLabelTagger = remoteBombLabelTaggerBuilderForItems.getTagger(item)
        val triggerTagger = triggerTaggerBuilderForItems.getTagger(item)
        if (remoteBombLabelTagger.hasValue() && triggerTagger.hasValue()) {
            uuidTaggerBuilderForItems.getTagger(item).value = null
        }
    }

    @EventHandler
    fun makeBombUnique(event: ItemSpawnEvent) {
        val remoteBombLabelTagger = remoteBombLabelTaggerBuilderForItems.getTagger(event.entity)
        val triggerTagger = triggerTaggerBuilderForItems.getTagger(event.entity)
        if (remoteBombLabelTagger.hasValue() && triggerTagger.hasValue()) {
            uuidTaggerBuilderForItems.getTagger(event.entity).value = UUID.randomUUID()
        }
    }

    @EventHandler
    fun triggerBomb(event: PlayerInteractEvent) {
        val labelTaggerOfTheHandItem = event.item?.itemMeta?.let { remoteBombLabelTaggerBuilder.getTagger(it) }
        val triggerTaggerOfTheHandItem = event.item?.itemMeta?.let { triggerTaggerBuilder.getTagger(it) }
        if (labelTaggerOfTheHandItem?.hasValue() == true && triggerTaggerOfTheHandItem?.hasValue() == false) {
            val labelInHand = labelTaggerOfTheHandItem.value
            for (remoteBombs in remoteBombsCollector.getRemoteBombs()) {
                if (labelInHand == remoteBombs.label) {
                    remoteBombs.trigger = 1
                }
            }
        }
    }
}
