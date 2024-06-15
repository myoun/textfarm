package app.myoun.textfarm.worldgen

import com.mojang.datafixers.kinds.App
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryOps
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ChunkRegion
import net.minecraft.world.HeightLimitView
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.biome.source.FixedBiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.Blender
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.VerticalBlockSample
import net.minecraft.world.gen.noise.NoiseConfig
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function


class VoidWorldGenerator(biomeRegistry: RegistryEntryLookup<Biome?>) :
    ChunkGenerator(FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS))) {
    override fun getCodec(): MapCodec<out ChunkGenerator?> {
        return CODEC
    }

    override fun carve(
        chunkRegion: ChunkRegion,
        l: Long,
        noiseConfig: NoiseConfig,
        biomeAccess: BiomeAccess,
        structureAccessor: StructureAccessor,
        chunk: Chunk,
        carver: GenerationStep.Carver,
    ) {
    }

    override fun buildSurface(
        region: ChunkRegion,
        structureAccessor: StructureAccessor,
        noiseConfig: NoiseConfig,
        chunk: Chunk,
    ) {
    }

    override fun populateEntities(region: ChunkRegion) {
    }

    override fun getWorldHeight(): Int {
        return 0
    }

    override fun populateNoise(
        executor: Executor?,
        blender: Blender?,
        noiseConfig: NoiseConfig?,
        structureAccessor: StructureAccessor?,
        chunk: Chunk,
    ): CompletableFuture<Chunk> {
        return CompletableFuture.completedFuture(chunk)
    }

    override fun getSeaLevel(): Int {
        return 0
    }

    override fun getMinimumY(): Int {
        return 0
    }

    override fun getHeight(
        x: Int,
        z: Int,
        heightmapType: Heightmap.Type,
        heightLimitView: HeightLimitView,
        noiseConfig: NoiseConfig,
    ): Int {
        return 0
    }

    override fun getColumnSample(
        x: Int,
        z: Int,
        heightLimitView: HeightLimitView,
        noiseConfig: NoiseConfig,
    ): VerticalBlockSample {
        return VerticalBlockSample(0, arrayOfNulls(0))
    }

    override fun getDebugHudText(list: List<String>, noiseConfig: NoiseConfig, blockPos: BlockPos) {
    }

    companion object {
        val CODEC: MapCodec<VoidWorldGenerator> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<VoidWorldGenerator> ->
                instance.group(RegistryOps.getEntryLookupCodec(RegistryKeys.BIOME))
                    .apply(
                        instance,
                        instance.stable(Function { biomeRegistry: RegistryEntryLookup<Biome?> ->
                            VoidWorldGenerator(
                                biomeRegistry
                            )
                        })
                    )
            }
    }
}