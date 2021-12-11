package com.gmail.noxdawn

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
    single(named("bomb")) { CommandSpec("bomb", BombCommandExecutor(get(named("bomb_count")))) }
    single(named("bombinfo")) { CommandSpec("bombinfo", BombInfoExecutor(get(named("bomb_count")))) }
    single<Listener> { BombEventListener(get(named("bomb_count")), get(named("bomb_unique")), get()) }
    single<Logger> { VerboseLoggerImpl(get()) }
    single { get<JavaPlugin>().logger }
    single<DropsCollector> { DropsCollectorImpl(get()) }
    single<TaskRegistry> { TaskRegistryImpl(get()) }
    single(createdAtStart = true) { BombController(get(), get(named("bomb_count")), get()) }
}
