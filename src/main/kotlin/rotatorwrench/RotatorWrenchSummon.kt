package rotatorwrench

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RotatorWrenchSummon : CommandExecutor {
    companion object {
        var outstandingWrenchItems: MutableSet<ItemStack> = mutableSetOf()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val wrenchItem = RotatorWrenchItem().createWrench()
        outstandingWrenchItems.add(wrenchItem)
        sender.inventory.addItem(wrenchItem)

        return true
    }
}