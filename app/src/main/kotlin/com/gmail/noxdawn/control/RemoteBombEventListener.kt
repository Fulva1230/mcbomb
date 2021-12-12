package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

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
    private val remoteBombsCollector: RemoteBombsCollector
) : Listener {
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
