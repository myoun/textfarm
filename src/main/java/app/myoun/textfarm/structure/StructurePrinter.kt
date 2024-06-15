package app.myoun.textfarm.structure

import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

object StructurePrinter {

    fun printStructure(nbt: NbtCompound, world: World, origin: BlockPos) {
        val blocks = nbt.getList("blocks", NbtCompound.COMPOUND_TYPE.toInt()).map { it as NbtCompound }
        val palettes = nbt.getList("palette", NbtCompound.COMPOUND_TYPE.toInt())
        val size = nbt.getList("size", NbtCompound.INT_TYPE.toInt())
        // entities are not supported

        val paletteMap = palettes.mapIndexed { index, nbtElement -> index to (nbtElement as NbtCompound).getString("Name") }.toMap()

        for (compound in blocks) {
            val pos = compound.getList("pos", NbtCompound.INT_TYPE.toInt()).let {
                Vec3i(it.getInt(0), it.getInt(1), it.getInt(2))
            }
            val state = compound.getInt("state")

            val type = Identifier.tryParse(paletteMap[state])

            val realPos = origin.add(pos)

            val block = Registries.BLOCK.get(type)
            val defaultState = block.defaultState

            world.setBlockState(realPos, defaultState)
        }
    }
}