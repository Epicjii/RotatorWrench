package rotatorwrench

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class RotatorWrenchItem {

    fun createWrench(): ItemStack {
        val wrench = ItemStack(Material.WOODEN_SWORD, 1)
        val meta = wrench.itemMeta
        meta.displayName(Component.text("Rotator Wrench"))
        meta.setCustomModelData(100)
        meta.lore(listOf(Component.text("Right Click A Rotatable Block")))
        meta.addEnchant(Enchantment.SWIFT_SNEAK, 1, false)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        wrench.setItemMeta(meta)
        return wrench
    }
}
