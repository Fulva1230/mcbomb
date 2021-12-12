package com.gmail.noxdawn.commands

import com.gmail.noxdawn.Tagger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BombInfoExecutor(
    private val bombTriggerTaggerBuilder: Tagger.Builder<Int>
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.server.getPlayer(sender.name)
        if (player != null) {
            val itemHold = player.inventory.itemInMainHand
            val tagger = bombTriggerTaggerBuilder.getTagger(itemHold.itemMeta!!)
            sender.sendMessage("the item is ${if (tagger.hasValue()) "" else "not "}a bomb")
            return true
        }
        return false
    }
}
