package com.gmail.noxdawn.commands

import com.gmail.noxdawn.Tagger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RemoteBombExecutor(
    private val remoteBombLabelTaggerBuilder: Tagger.Builder<String>,
    private val triggerTaggerBuilder: Tagger.Builder<Int>,
    private val powerTaggerBuilder: Tagger.Builder<Double>,
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        val label1 = args.getOrNull(0)
        val power = args.getOrNull(1)?.toDoubleOrNull() ?: 3.0
        if (label1 != null && player != null) {
            val itemInMainHand = player.inventory.itemInMainHand
            val itemMetaCopy = itemInMainHand.itemMeta
            itemInMainHand.itemMeta = itemMetaCopy?.also { notNullItemMetaCopy ->
                remoteBombLabelTaggerBuilder.getTagger(notNullItemMetaCopy).value = label1
                triggerTaggerBuilder.getTagger(notNullItemMetaCopy).value = 0
                powerTaggerBuilder.getTagger(notNullItemMetaCopy).value = power
            }
            return true
        }
        return false
    }
}
