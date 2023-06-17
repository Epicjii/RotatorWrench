package rotatorwrench

import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class RotatorWrenchPlugin : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(RotatorWrenchLogic(), this)
        getCommand("rotator")?.setExecutor(RotatorWrenchSummon())
    }

    override fun onDisable() {
        HandlerList.unregisterAll()
    }
}