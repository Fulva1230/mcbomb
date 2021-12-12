package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item

class BombCountDownTrigger(
    private val item: Item,
    private val countTagger: Tagger<Int>,
    private val triggerTagger: Tagger<Int>,
) : CountDownTimer {
    override fun countDown() {
        if (countTagger.value > 0) {
            --countTagger.value
        }
        item.customName = "${(countTagger.value + 9) / 10}"
        if (countTagger.value == 0) {
            triggerTagger.value = 1
            countTagger.value = -1
        }
    }
}
