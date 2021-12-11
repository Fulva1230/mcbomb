package com.gmail.noxdawn

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombCommandExecutor(private val bombCountTaggerBuilder: Tagger.Builder<Int>) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        val secs = args.getOrNull(0)?.toInt()
        if (player != null) {
            val itemInHand = player.inventory.itemInMainHand
            val itemMetaInHand = itemInHand.itemMeta
            if (itemMetaInHand != null) {
                val tagger = bombCountTaggerBuilder.getTagger(itemMetaInHand)
                tagger.value = secs?.let { it * 10 } ?: 30
                itemInHand.itemMeta = itemMetaInHand
                sender.sendMessage("Successfully set")
                return true
            }
        }
        return false
    }
}
