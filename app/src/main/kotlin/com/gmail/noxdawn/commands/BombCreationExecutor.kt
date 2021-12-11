package com.gmail.noxdawn.commands

import com.gmail.noxdawn.Tagger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombCreationExecutor(
    private val bombCountTaggerBuilder: Tagger.Builder<Int>,
    private val bombPowerTaggerBuilder: Tagger.Builder<Double>,
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        val secs = args.getOrNull(0)?.toInt()
        val power = args.getOrNull(1)?.toDouble()
        if (player != null) {
            val itemInHand = player.inventory.itemInMainHand
            val itemMetaInHand = itemInHand.itemMeta
            if (itemMetaInHand != null) {
                val bombCountDownTagger = bombCountTaggerBuilder.getTagger(itemMetaInHand)
                bombCountDownTagger.value = secs?.let { it * 10 } ?: 30
                val bombPowerTagger = bombPowerTaggerBuilder.getTagger(itemMetaInHand)
                bombPowerTagger.value = power ?: 3.0
                itemInHand.itemMeta = itemMetaInHand
                sender.sendMessage("Successfully set")
                return true
            }
        }
        return false
    }
}
