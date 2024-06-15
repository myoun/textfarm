@file:JvmName("Commands")
package app.myoun.textfarm

import app.myoun.textfarm.TextFarmUtil.dbPlayer
import app.myoun.textfarm.TextFarmUtil.luckpermsUser
import app.myoun.textfarm.data.service.IslandService
import app.myoun.textfarm.structure.StructurePrinter
import com.mojang.brigadier.Command
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtSizeTracker
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

val FarmCommand = literal("farm")
    .then(
        literal("create")
            .requires { source ->
                source.isExecutedByPlayer && source.player?.dbPlayer != null && source.playerOrThrow.luckpermsUser
                    ?.cachedData
                    ?.permissionData
                    ?.checkPermission("textfarm.farm.create")
                    ?.asBoolean() ?: false
            }
            .executes { context ->
                // 섬 생성 후 섬 위치 및 섬 생성 정보 저장
                val server = TextFarmServer.server
                val resourceManager = server.resourceManager
                val dbPlayer = context.source.playerOrThrow.dbPlayer!!
                val farmStructure = resourceManager.getResource(Identifier(TextFarmServer.MODID, "map_templates/farm.nbt"))
                val compound = NbtIo.readCompressed(farmStructure.get().inputStream, NbtSizeTracker.ofUnlimitedBytes())
                println(1)
                try {
                    if (IslandService.playerHasIsland(dbPlayer)) {
                        // Player has island
                        context.source.sendFeedback({ ->
                            Text.of("You already have your island.")
                        }, false)
                        return@executes 0
                    } else {
                        IslandService.createNewIsland(dbPlayer)
                        context.source.sendFeedback({ ->
                            Text.of("Island Created!")
                        }, false)

                        val coordinate = IslandService.getIslandCoordinate(dbPlayer)
                        StructurePrinter.printStructure(compound, TextFarmServer.farmWorld, BlockPos(coordinate.first,100, coordinate.second))
                        return@executes Command.SINGLE_SUCCESS
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@executes 1
                }

            }
    ).then(
        literal("border")
            .executes { context ->
                val player = context.source.player ?: return@executes 0
                TextFarmUtil.setPlayerBorder(player, 0.0 to 0.0, 100.0)
                1
            }
    ).then(
        literal("tp")
            .requires { IslandService.playerHasIsland(it.player?.dbPlayer!!) }
            .executes { context ->
                val player = context.source.player ?: return@executes 0
                val xZCoordinate = IslandService.getIslandCoordinate(player.dbPlayer!!)
                player.moveToWorld(TextFarmServer.farmWorld)
                player.teleport(xZCoordinate.first.toDouble(), 100.0, xZCoordinate.second.toDouble())
                1
            }
    )