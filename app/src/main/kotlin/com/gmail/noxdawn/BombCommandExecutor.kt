package com.gmail.noxdawn

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombCommandExecutor(private val integerTaggerBuilder: IntegerTagger.Builder) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        if (player != null) {
            val itemMetaInHand = player.inventory.itemInMainHand.itemMeta
            if (itemMetaInHand != null) {
                val tagger = integerTaggerBuilder.getTagger(itemMetaInHand)
                tagger.value = 3
                return true
            }
        }
        return false
    }
}
