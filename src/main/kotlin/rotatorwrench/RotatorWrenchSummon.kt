package rotatorwrench

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RotatorWrenchSummon : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val wrenchItem = RotatorWrenchItem().createWrench()
        sender.inventory.addItem(wrenchItem)

        return true
    }
}