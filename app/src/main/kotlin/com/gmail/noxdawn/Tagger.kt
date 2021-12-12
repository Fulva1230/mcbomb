package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface Tagger<T> {
    var value: T
    fun hasValue(): Boolean

    interface Builder<T> {
        fun getTagger(item: PersistentDataHolder): Tagger<T>
    }

    interface BuilderForItems<T> {
        fun getTagger(item: Item): Tagger<T>
    }
}

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

class ItemIntegerTaggerImpl private constructor(
    private val key: NamespacedKey, private val item: Item
) : Tagger<Int> {
    override var value: Int
        get() = item.itemStack.itemMeta?.persistentDataContainer?.getOrDefault(
            key, PersistentDataType.INTEGER, 0
        ) ?: 0
        set(value) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { notNullItemMetaCopy ->
                notNullItemMetaCopy.persistentDataContainer.set(key, PersistentDataType.INTEGER, value)
            }
        }

    override fun hasValue(): Boolean =
        item.itemStack.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.INTEGER) == true


    class BuilderImpl(private val key: NamespacedKey) : Tagger.BuilderForItems<Int> {
        override fun getTagger(item: Item): Tagger<Int> = ItemIntegerTaggerImpl(key, item)
    }
}

class ItemDoubleTaggerImpl private constructor(
    private val key: NamespacedKey, private val item: Item
) : Tagger<Double> {
    override var value: Double
        get() = item.itemStack.itemMeta?.persistentDataContainer?.getOrDefault(
            key, PersistentDataType.DOUBLE, 0.0
        ) ?: 0.0
        set(value) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { notNullItemMetaCopy ->
                notNullItemMetaCopy.persistentDataContainer.set(key, PersistentDataType.DOUBLE, value)
            }
        }

    override fun hasValue(): Boolean =
        item.itemStack.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.DOUBLE) == true


    class BuilderImpl(private val key: NamespacedKey) : Tagger.BuilderForItems<Double> {
        override fun getTagger(item: Item): Tagger<Double> = ItemDoubleTaggerImpl(key, item)
    }
}

class ItemUUIDTaggerImpl private constructor(
    private val key: NamespacedKey, private val item: Item
) : Tagger<UUID> {
    override var value: UUID
        get() {
            val uuidString = item.itemStack.itemMeta?.persistentDataContainer?.get(key, PersistentDataType.STRING)
            if (uuidString != null) {
                return UUID.fromString(uuidString)
            } else {
                return UUID.randomUUID()
            }
        }
        set(value) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { notNullItemMetaCopy ->
                notNullItemMetaCopy.persistentDataContainer.set(key, PersistentDataType.STRING, value.toString())
            }
        }

    override fun hasValue(): Boolean =
        item.itemStack.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.STRING) == true


    class BuilderImpl(private val key: NamespacedKey) : Tagger.BuilderForItems<UUID> {
        override fun getTagger(item: Item): Tagger<UUID> = ItemUUIDTaggerImpl(key, item)
    }
}


