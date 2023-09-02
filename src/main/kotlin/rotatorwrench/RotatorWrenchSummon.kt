package rotatorwrench

import net.kyori.adventure.text.Component
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
        if (!sender.inventory.contains(wrenchItem)) {
            sender.inventory.addItem(wrenchItem)
        }
        val wrenchIndex = sender.inventory.first(wrenchItem)
        if (wrenchIndex == -1) {
            sender.sendMessage(Component.text("Hey! Your Inventory is Full!"))
            return true
        }
        sender.inventory.setItem(wrenchIndex, sender.inventory.itemInMainHand)
        sender.inventory.setItemInMainHand(wrenchItem)

        return true
    }
}