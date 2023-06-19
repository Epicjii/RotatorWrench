package rotatorwrench

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockSupport
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable
import org.bukkit.block.data.Rail
import org.bukkit.block.data.type.Slab
import org.bukkit.block.data.type.Stairs
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
        if (event.item?.itemMeta != RotatorWrenchItem.itemMeta) {
            return
        }
        val clickedBlock = event.clickedBlock

        if (clickedBlock != null) {
            when (val blockData = clickedBlock.blockData) {
                is Directional -> {
                    if (clickedBlock.type == Material.END_PORTAL_FRAME) {
                        return
                    }
                    if (blockData is Stairs) {
                        clickedBlock.blockData = stairLogic(blockData)
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

                is Slab -> {
                    clickedBlock.blockData = slabLogic(blockData)
                }
            }
        }
    }

    private fun slabLogic(blockData: Slab): Slab {
        if (blockData.type == Slab.Type.DOUBLE) {
            return blockData
        }
        val type = Slab.Type.values().filter {
            it != Slab.Type.DOUBLE
        }
        val currentType = type.indexOf(blockData.type)
        blockData.type = type.elementAt((currentType + 1) % type.size)
        return blockData
    }

    private fun stairLogic(blockData: Stairs): Stairs {
        val iterations = mutableListOf<Pair<Half, BlockFace>>()
        for (face in blockData.faces) {
            iterations.add(Pair(Half.TOP, face))
            iterations.add(Pair(Half.BOTTOM, face))
        }
        val facing = blockData.facing
        val half = blockData.half

        val currentIndex = iterations.indexOf(Pair(half, facing))
        val next = iterations.elementAt((currentIndex + 1) % iterations.size)
        blockData.half = next.first
        blockData.facing = next.second
        return blockData
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
            for (item in event.inventory) {
                if (item.itemMeta == RotatorWrenchItem.itemMeta) {
                    event.inventory.remove(item)
                }
            }
        }
    }

    @EventHandler
    private fun dropToRemove(event: PlayerDropItemEvent) {
        val dropped = event.itemDrop.itemStack
        if (dropped.itemMeta == RotatorWrenchItem.itemMeta) {
            event.itemDrop.remove()
        }
    }
}