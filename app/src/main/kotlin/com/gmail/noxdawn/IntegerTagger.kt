package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType

interface IntegerTagger {
    var value: Int
    fun hasValue(): Boolean

    interface Builder {
        fun getTagger(item: PersistentDataHolder): IntegerTagger
    }
}

class IntegerTaggerImpl private constructor(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
) : IntegerTagger {
    override var value: Int
        get() = item.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, -1)
        set(value) {
            item.persistentDataContainer.set(key, PersistentDataType.INTEGER, value)
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, PersistentDataType.INTEGER)

    class BuilderImpl(private val key: NamespacedKey) : IntegerTagger.Builder {
        override fun getTagger(item: PersistentDataHolder): IntegerTagger {
            return IntegerTaggerImpl(key, item)
        }
    }
}
