package rotatorwrench

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockSupport
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable
import org.bukkit.block.data.Rail
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class RotatorWrenchLogic : Listener {

    @EventHandler
    private fun onRightClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        if (!RotatorWrenchSummon.outstandingWrenchItems.contains(event.player.inventory.itemInMainHand)) {
            return
        }
        val clickedBlock = event.clickedBlock

        if (clickedBlock != null) {
            when (val blockData = clickedBlock.blockData) {
                is Directional -> {
                    if (clickedBlock.type == Material.END_PORTAL_FRAME) {
                        return
                    }
                    clickedBlock.blockData = directionalLogic(blockData)
                }

                is Rail -> {
                    clickedBlock.blockData = railLogic(blockData, clickedBlock)
                }

                is Orientable -> {
                    clickedBlock.blockData = orientableLogic(blockData)
                }
            }
        }
    }

    private fun orientableLogic(blockData: Orientable): Orientable {
        val axis = blockData.axes
        val currentAxisIndex = axis.indexOf(blockData.axis)
        blockData.axis = axis.elementAt((currentAxisIndex + 1) % axis.size)
        return blockData
    }

    private fun railLogic(rail: Rail, block: Block): Rail {
        val shapes = rail.shapes.toMutableList()
        shapes.removeAll(
            listOf(
                Rail.Shape.ASCENDING_EAST,
                Rail.Shape.ASCENDING_NORTH,
                Rail.Shape.ASCENDING_SOUTH,
                Rail.Shape.ASCENDING_WEST
            )
        )
        if (block.getRelative(BlockFace.NORTH).blockData.isFaceSturdy(BlockFace.UP, BlockSupport.RIGID)) {
            shapes.add(Rail.Shape.ASCENDING_NORTH)
        }
        if (block.getRelative(BlockFace.EAST).blockData.isFaceSturdy(BlockFace.UP, BlockSupport.RIGID)) {
            shapes.add(Rail.Shape.ASCENDING_EAST)
        }
        if (block.getRelative(BlockFace.SOUTH).blockData.isFaceSturdy(BlockFace.UP, BlockSupport.RIGID)) {
            shapes.add(Rail.Shape.ASCENDING_SOUTH)
        }
        if (block.getRelative(BlockFace.WEST).blockData.isFaceSturdy(BlockFace.UP, BlockSupport.RIGID)) {
            shapes.add(Rail.Shape.ASCENDING_WEST)
        }
        val currentShapeIndex = shapes.indexOf(rail.shape)
        rail.shape = shapes.elementAt((currentShapeIndex + 1) % shapes.size)
        return rail
    }

    private fun directionalLogic(directionalBlock: Directional): BlockData {
        val faces = directionalBlock.faces
        val currentFaceIndex = faces.indexOf(directionalBlock.facing)
        directionalBlock.facing = faces.elementAt((currentFaceIndex + 1) % faces.size)
        return directionalBlock
    }

    @EventHandler
    private fun depositToRemove(event: InventoryCloseEvent) {
        if (event.inventory.holder != event.player) {
            for (wrench in RotatorWrenchSummon.outstandingWrenchItems) {
                event.inventory.remove(wrench)
            }
        }
    }

    @EventHandler
    private fun dropToRemove(event: PlayerDropItemEvent) {
        val dropped = event.itemDrop.itemStack
        if (RotatorWrenchSummon.outstandingWrenchItems.contains(dropped)) {
            event.itemDrop.remove()
        }
    }
}