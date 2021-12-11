package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface Tagger<T> {
    var value: T
    fun hasValue(): Boolean

    interface Builder<T> {
        fun getTagger(item: PersistentDataHolder): Tagger<T>
    }
}

class UUIDTaggerImpl private constructor(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
) : Tagger<UUID> {
    override var value: UUID
        get() {
            val uuidString = item.persistentDataContainer.get(key, PersistentDataType.STRING)
            return uuidString?.let { UUID.fromString(it) } ?: UUID.randomUUID()
        }
        set(value) {
            item.persistentDataContainer.set(key, PersistentDataType.STRING, value.toString())
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, PersistentDataType.STRING)

    class BuilderImpl(private val key: NamespacedKey) : Tagger.Builder<UUID> {
        override fun getTagger(item: PersistentDataHolder): Tagger<UUID> = UUIDTaggerImpl(key, item)
    }
}

class DoubleTaggerImpl private constructor(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
) : Tagger<Double> {
    override var value: Double
        get() {
            return item.persistentDataContainer.getOrDefault(key, PersistentDataType.DOUBLE, 0.0)
        }
        set(value) {
            item.persistentDataContainer.set(key, PersistentDataType.DOUBLE, value)
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, PersistentDataType.DOUBLE)

    class BuilderImpl(private val key: NamespacedKey) : Tagger.Builder<Double> {
        override fun getTagger(item: PersistentDataHolder): Tagger<Double> = DoubleTaggerImpl(key, item)
    }
}
