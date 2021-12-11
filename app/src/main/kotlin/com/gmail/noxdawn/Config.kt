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
    single<Tagger.Builder<Int>>(named("bomb_count")) {
        IntegerTaggerImpl.BuilderImpl(
            NamespacedKey(
                get<JavaPlugin>(), "bomb_count"
            )
        )
    }
    single<Tagger.Builder<UUID>>(named("bomb_unique")) {
        UUIDTaggerImpl.BuilderImpl(NamespacedKey(get<JavaPlugin>(), "bomb_unique"))
    }
    single<Tagger.Builder<Double>>(named("bomb_power")) {
        DoubleTaggerImpl.BuilderImpl(NamespacedKey(get<JavaPlugin>(), "bomb_power"))
    }
    single(named("bomb")) {
        CommandSpec(
            "bomb",
            BombCreationExecutor(get(named("bomb_count")), get(named("bomb_power")))
        )
    }
    single(named("bombinfo")) { CommandSpec("bombinfo", BombInfoExecutor(get(named("bomb_count")))) }
    single<Listener> { BombEventListener(get(named("bomb_count")), get(named("bomb_unique")), get()) }
    single<Logger> { VerboseLoggerImpl(get()) }
    single { get<JavaPlugin>().logger }
    single<DropsCollector> { DropsCollectorImpl(get()) }
    single<TaskRegistry> { TaskRegistryImpl(get()) }
    single<Controller> { BombController(get(), get(named("bomb_count")), get(named("bomb_power"))) }
    single(createdAtStart = true) { Runner(get(), getAll()) }
}
