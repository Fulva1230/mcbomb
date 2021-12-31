package com.gmail.noxdawn.commands

import com.gmail.noxdawn.Tagger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombCreationExecutor(
    private val bombCountTaggerBuilder: Tagger.Builder<Int>,
    private val bombPowerTaggerBuilder: Tagger.Builder<Double>,
    private val bombTriggerTaggerBuilder: Tagger.Builder<Int>
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        val secs = args.getOrNull(0)?.toIntOrNull()
        val power = args.getOrNull(1)?.toDoubleOrNull()
        if (player != null) {
            val itemInHand = player.inventory.itemInMainHand
            val itemMetaInHandCopy = itemInHand.itemMeta
            if (itemMetaInHandCopy != null) {
                val bombCountDownTagger = bombCountTaggerBuilder.getTagger(itemMetaInHandCopy)
                bombCountDownTagger.value = secs?.let { it * 10 } ?: 30
                val bombPowerTagger = bombPowerTaggerBuilder.getTagger(itemMetaInHandCopy)
                bombPowerTagger.value = power ?: 3.0
                val bombTriggerTagger = bombTriggerTaggerBuilder.getTagger(itemMetaInHandCopy)
                bombTriggerTagger.value = 0
                itemInHand.itemMeta = itemMetaInHandCopy
                sender.sendMessage("Successfully set")
                return true
            }
        }
        return false
    }
}
