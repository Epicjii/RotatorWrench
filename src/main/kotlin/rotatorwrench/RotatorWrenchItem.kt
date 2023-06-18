package rotatorwrench

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class RotatorWrenchItem {

    companion object {
        val itemMeta : ItemMeta
        init {
            val wrench = ItemStack(Material.WOODEN_SWORD, 1)
            val meta = wrench.itemMeta
            meta.displayName(Component.text("Rotator Wrench"))
            meta.setCustomModelData(100)
            meta.lore(listOf(Component.text("Right Click A Rotatable Block")))
            meta.addEnchant(Enchantment.SWIFT_SNEAK, 1, false)
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            itemMeta = meta
        }
    }

    fun createWrench(): ItemStack {
        val wrench = ItemStack(Material.WOODEN_SWORD, 1)
        wrench.setItemMeta(itemMeta)
        return wrench
    }
}
