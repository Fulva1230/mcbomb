package com.gmail.noxdawn.commands

import com.gmail.noxdawn.Tagger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombRemoteExecutor (
    private val remoteBombLabelTaggerBuilder: Tagger.Builder<String>,
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        val label1 = args.getOrNull(0)
        if (label1 != null && player != null) {
            val itemInMainHand = player.inventory.itemInMainHand
            val itemMetaCopy = itemInMainHand.itemMeta
            itemInMainHand.itemMeta = itemMetaCopy?.also { notNullItemMetaCopy ->
                remoteBombLabelTaggerBuilder.getTagger(notNullItemMetaCopy).value = label1
            }
            return true
        }
        return false
    }
}
