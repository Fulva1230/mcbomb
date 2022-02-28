package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger
import org.bukkit.entity.Item

class BombCountDownTrigger(
    private val item: Item,
    private val countTagger: Tagger<Int>,
    private val triggerTagger: Tagger<Int>,
) : CountDownTimer {
    override fun countDown() {
        var countValue = countTagger.value
        if (countValue != null && countValue > 0) {
            --countValue
            countTagger.value = countValue
            item.customName = "${(countValue + 9) / 10}"
        }
        if (countValue == 0) {
            triggerTagger.value = 1
            countTagger.value = -1
        }
    }
}
