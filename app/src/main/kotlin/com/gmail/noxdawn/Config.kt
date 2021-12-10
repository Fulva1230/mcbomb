package com.gmail.noxdawn

import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.bind
import org.koin.dsl.module

val module = module {
    single {
        IntegerTaggerImpl.BuilderImpl(
            NamespacedKey(
                get<JavaPlugin>(),
                "bomb_count"
            )
        )
    } bind IntegerTagger.Builder::class
    single { CommandSpec("bomb", BombCommandExecutor(get())) }
    single { EventListener(get(), get()) } bind Listener::class
}
