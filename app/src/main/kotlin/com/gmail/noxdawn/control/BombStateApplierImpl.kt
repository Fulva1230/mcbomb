package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin

class BombStateApplierImpl(
    private val plugin: JavaPlugin,
    private val countDownTaggerBuilder: Tagger.Builder<Int>,
    private val powerTaggerBuilder: Tagger.Builder<Double>,
) : BombStateApplier {
    override fun apply(currentState: BombState, nextState: BombState) {
        val bombEntity = plugin.server.getEntity(currentState.id) as? Item
        val itemMetaCopy = bombEntity?.itemStack?.itemMeta
        if (bombEntity != null && itemMetaCopy != null) {
            val countDownTagger = countDownTaggerBuilder.getTagger(itemMetaCopy)
            val powerTagger = powerTaggerBuilder.getTagger(itemMetaCopy)
            countDownTagger.value = nextState.count
            powerTagger.value = nextState.power
            bombEntity.customName = "${(nextState.count + 9) / 10}"
            if (nextState.shouldExplode) {
                bombEntity.world.createExplosion(bombEntity.location, nextState.power.toFloat())
                bombEntity.remove()
            }
            bombEntity.itemStack.itemMeta = itemMetaCopy
        }
    }
}
