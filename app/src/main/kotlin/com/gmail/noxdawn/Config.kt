package com.gmail.noxdawn

import com.gmail.noxdawn.commands.BombCreationExecutor
import com.gmail.noxdawn.commands.BombInfoExecutor
import com.gmail.noxdawn.commands.CommandSpec
import com.gmail.noxdawn.control.*
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

val intTaggerSuite: Module.(name: String) -> Unit = { name ->
    single(named(name)) { NamespacedKey(get<JavaPlugin>(), name) }
    single<Tagger.BuilderForItems<Int>>(named(name)) {
        ItemTaggerImpl.IntegerBuilderImpl(
            get(named(name))
        )
    }
    single<Tagger.Builder<Int>>(named(name)) {
        TaggerImpl.IntegerBuilderImpl(
            get(named(name))
        )
    }
}

val doubleTaggerSuite: Module.(name: String) -> Unit = { name ->
    single(named(name)) { NamespacedKey(get<JavaPlugin>(), name) }
    single<Tagger.BuilderForItems<Double>>(named(name)) {
        ItemTaggerImpl.DoubleBuilderImpl(
            get(named(name))
        )
    }
    single<Tagger.Builder<Double>>(named(name)) {
        TaggerImpl.DoubleBuilderImpl(
            get(named(name))
        )
    }
}

val uuidTaggerSuite: Module.(name: String) -> Unit = { name ->
    single(named(name)) { NamespacedKey(get<JavaPlugin>(), name) }
    single<Tagger.BuilderForItems<UUID>>(named(name)) {
        ItemUUIDTaggerImpl.BuilderImpl(
            get(named(name))
        )
    }
    single<Tagger.Builder<UUID>>(named(name)) {
        UUIDTaggerImpl.BuilderImpl(
            get(named(name))
        )
    }
}


val module = module {
    intTaggerSuite("bomb_count")
    uuidTaggerSuite("bomb_unique")
    doubleTaggerSuite("bomb_power")
    intTaggerSuite("bomb_trigger")
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
    single<TriggerCollector>(named("explosive_collector")) {
        ExplosiveCollector(
            get(), get(named("bomb_trigger")), get(named("bomb_power"))
        )
    }
    single<TaskRegistry> { TaskRegistryImpl(get()) }
    single(createdAtStart = true) { Runner(get(), getAll()) }
}
