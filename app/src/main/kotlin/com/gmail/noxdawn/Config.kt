package com.gmail.noxdawn

import com.gmail.noxdawn.commands.BombCreationExecutor
import com.gmail.noxdawn.commands.BombInfoExecutor
import com.gmail.noxdawn.commands.CommandSpec
import com.gmail.noxdawn.control.*
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

val module = module {
    single(named("bomb_count")) { NamespacedKey(get<JavaPlugin>(), "bomb_count") }
    single<Tagger.BuilderForItems<Int>>(named("bomb_count")) {
        ItemIntegerTaggerImpl.BuilderImpl(
            get(named("bomb_count"))
        )
    }
    single<Tagger.Builder<Int>>(named("bomb_count")) {
        IntegerTaggerImpl.BuilderImpl(
            get(named("bomb_count"))
        )
    }
    single<Tagger.BuilderForItems<UUID>>(named("bomb_unique")) {
        ItemUUIDTaggerImpl.BuilderImpl(NamespacedKey(get<JavaPlugin>(), "bomb_unique"))
    }
    single(named("bomb_power")) { NamespacedKey(get<JavaPlugin>(), "bomb_power") }
    single<Tagger.BuilderForItems<Double>>(named("bomb_power")) {
        ItemDoubleTaggerImpl.BuilderImpl(get(named("bomb_power")))
    }
    single<Tagger.Builder<Double>>(named("bomb_power")) { DoubleTaggerImpl.BuilderImpl(get(named("bomb_power"))) }
    single(named("bomb_trigger")) { NamespacedKey(get<JavaPlugin>(), "bomb_trigger") }
    single<Tagger.BuilderForItems<Int>>(named("bomb_trigger")) {
        ItemIntegerTaggerImpl.BuilderImpl(get(named("bomb_trigger")))
    }
    single<Tagger.Builder<Int>>(named("bomb_trigger")) {
        IntegerTaggerImpl.BuilderImpl(get(named("bomb_trigger")))
    }
    single(named("bomb")) {
        CommandSpec(
            "bomb", BombCreationExecutor(get(named("bomb_count")), get(named("bomb_power")), get(named("bomb_trigger")))
        )
    }
    single(named("bombinfo")) { CommandSpec("bombinfo", BombInfoExecutor(get(named("bomb_trigger")))) }
    single<Listener> { BombEventListener(get(named("bomb_count")), get(named("bomb_unique")), get()) }
    single<Logger> { VerboseLoggerImpl(get()) }
    single { get<JavaPlugin>().logger }
    single<Controller>(named("countdown_controller")) { CountDownController(get()) }
    single<CountDownTimerCollector> {
        BombCountDownTriggerCollector(
            get(), get(named("bomb_trigger")), get(named("bomb_count"))
        )
    }
    single<Controller>(named("trigger_controller")) { TriggerController(getAll()) }
    single<TriggerCollector>(named("explosive_collector")) { ExplosiveCollector(get(), get(named("bomb_trigger")), get(named("bomb_power"))) }
    single<TaskRegistry> { TaskRegistryImpl(get()) }
    single(createdAtStart = true) { Runner(get(), getAll()) }
}
