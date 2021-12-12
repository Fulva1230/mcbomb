package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType

class IntegerTaggerImpl private constructor(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
) : Tagger<Int> {
    override var value: Int
        get() = item.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0)
        set(value) {
            item.persistentDataContainer.set(key, PersistentDataType.INTEGER, value)
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, PersistentDataType.INTEGER)

    class BuilderImpl(private val key: NamespacedKey) : Tagger.Builder<Int> {
        override fun getTagger(item: PersistentDataHolder): Tagger<Int> {
            return IntegerTaggerImpl(key, item)
        }
    }
}
