package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item

interface DropsCollector {
    fun getDrops(): Iterable<Item>
}

class BombController(
    private val dropsCollector: DropsCollector,
    private val countDownTaggerBuilder: Tagger.Builder<Int>,
) : Controller {
    override val period: Int = 2

    override fun activate() {
        for (item in dropsCollector.getDrops()) {
            ItemUpdate(item).update()
        }
    }

    private inner class ItemUpdate(private val item: Item) {
        private val itemMetaCopy = item.itemStack.itemMeta
        private val countdownTagger = itemMetaCopy?.let { countDownTaggerBuilder.getTagger(it) }

        fun update() {
            countdownTagger?.let { countdownTagger ->
                if (countdownTagger.hasValue() && countdownTagger.value > 0) {
                    --countdownTagger.value
                    item.customName = "${(countdownTagger.value + 9) / 10}"
                }
                if (countdownTagger.value == 0) {
                    explode()
                    item.remove()
                    countdownTagger.value = -1
                }
                item.itemStack.itemMeta = itemMetaCopy
            }
        }

        private fun explode() {
            item.world.createExplosion(item.location, 3.0f)
        }
    }
}
