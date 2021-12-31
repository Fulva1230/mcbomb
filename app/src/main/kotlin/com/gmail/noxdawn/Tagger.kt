package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface Tagger<T : Any> {
    var value: T?
    fun hasValue(): Boolean

    interface Builder<T : Any> {
        fun getTagger(item: PersistentDataHolder): Tagger<T>
        fun getTagger(item: Item): Tagger<T>
    }
}

private class TaggerImpl<T, Z : Any>(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
    private val dataType: PersistentDataType<T, Z>,
) : Tagger<Z> {
    override var value: Z?
        get() = item.persistentDataContainer.get(key, dataType)
        set(value) {
            if (value == null) {
                item.persistentDataContainer.remove(key)
            } else {
                item.persistentDataContainer.set(key, dataType, value)
            }
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, dataType)
}

private class ItemTaggerImpl<T, Z : Any>(
    private val key: NamespacedKey,
    private val item: Item,
    private val dataType: PersistentDataType<T, Z>,
) : Tagger<Z> {
    override var value: Z?
        get() = item.itemStack.itemMeta?.persistentDataContainer?.get(
            key, dataType
        )
        set(value) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { notNullItemMetaCopy ->
                if (value == null) {
                    notNullItemMetaCopy.persistentDataContainer.remove(key)
                } else {
                    notNullItemMetaCopy.persistentDataContainer.set(key, dataType, value)
                }
            }
        }

    override fun hasValue(): Boolean = item.itemStack.itemMeta?.persistentDataContainer?.has(key, dataType) == true
}

class IntegerTaggerBuilderImpl(private val key: NamespacedKey) : Tagger.Builder<Int> {
    override fun getTagger(item: PersistentDataHolder): Tagger<Int> = TaggerImpl(key, item, PersistentDataType.INTEGER)
    override fun getTagger(item: Item): Tagger<Int> = ItemTaggerImpl(key, item, PersistentDataType.INTEGER)
}

class DoubleTaggerBuilderImpl(private val key: NamespacedKey) : Tagger.Builder<Double>{
    override fun getTagger(item: PersistentDataHolder): Tagger<Double> = TaggerImpl(key, item, PersistentDataType.DOUBLE)
    override fun getTagger(item: Item): Tagger<Double> = ItemTaggerImpl(key, item, PersistentDataType.DOUBLE)
}

class StringTaggerBuilderImpl(private val key: NamespacedKey) : Tagger.Builder<String>{
    override fun getTagger(item: PersistentDataHolder): Tagger<String> = TaggerImpl(key, item, PersistentDataType.STRING)
    override fun getTagger(item: Item): Tagger<String> = ItemTaggerImpl(key, item, PersistentDataType.STRING)
}

class UUIDTaggerBuilderImpl(private val key: NamespacedKey) : Tagger.Builder<UUID>{
    override fun getTagger(item: PersistentDataHolder): Tagger<UUID> = UUIDTaggerImpl(key, item)
    override fun getTagger(item: Item): Tagger<UUID> = ItemUUIDTaggerImpl(key, item)
}

private class UUIDTaggerImpl constructor(
    private val key: NamespacedKey,
    private val item: PersistentDataHolder,
) : Tagger<UUID> {
    override var value: UUID?
        get() {
            val uuidString = item.persistentDataContainer.get(key, PersistentDataType.STRING)
            return uuidString?.let { UUID.fromString(it) }
        }
        set(value) {
            if (value == null) {
                item.persistentDataContainer.remove(key)
            } else {
                item.persistentDataContainer.set(key, PersistentDataType.STRING, value.toString())
            }
        }

    override fun hasValue(): Boolean = item.persistentDataContainer.has(key, PersistentDataType.STRING)
}

private class ItemUUIDTaggerImpl constructor(
    private val key: NamespacedKey, private val item: Item
) : Tagger<UUID> {
    override var value: UUID?
        get() {
            val uuidString = item.itemStack.itemMeta?.persistentDataContainer?.get(key, PersistentDataType.STRING)
            if (uuidString != null) {
                return UUID.fromString(uuidString)
            } else {
                return null
            }
        }
        set(value) {
            item.itemStack.itemMeta = item.itemStack.itemMeta?.also { notNullItemMetaCopy ->
                if (value == null) {
                    notNullItemMetaCopy.persistentDataContainer.remove(key)
                } else {
                    notNullItemMetaCopy.persistentDataContainer.set(key, PersistentDataType.STRING, value.toString())
                }
            }
        }

    override fun hasValue(): Boolean =
        item.itemStack.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.STRING) == true
}


