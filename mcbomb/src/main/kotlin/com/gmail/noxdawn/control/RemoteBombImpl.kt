package com.gmail.noxdawn.control

import com.gmail.noxdawn.Tagger

class RemoteBombImpl(
    private val labelTagger: Tagger<String>, private val triggerTagger: Tagger<Int>
) : RemoteBomb {
    override var label: String
        get() = labelTagger.value ?: ""
        set(value) {
            labelTagger.value = value
        }
    override var trigger: Int
        get() = triggerTagger.value ?: 0
        set(value) {
            triggerTagger.value = value
        }
}
