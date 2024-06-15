package app.myoun.textfarm

import app.myoun.textfarm.TextFarmUtil.dbPlayer
import app.myoun.textfarm.data.*
import app.myoun.textfarm.data.dao.Islands
import app.myoun.textfarm.data.dao.Players
import app.myoun.textfarm.data.service.PlayerService
import app.myoun.textfarm.worldgen.VoidWorldGenerator
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.luckperms.api.LuckPermsProvider
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.dimension.DimensionType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object TextFarmServer : DedicatedServerModInitializer {

    val MODID = "textfarm"

    private val FARM_DIMENSION_KEY: RegistryKey<DimensionOptions> = RegistryKey.of(
        RegistryKeys.DIMENSION,
        Identifier(MODID, "farm")
    )


    private val FARM_WORLD_KEY: RegistryKey<World> by lazy {
        RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier(MODID, "farm")
        )
    }

    private val FARM_DIMENSION_TYPE_KEY: RegistryKey<DimensionType> = RegistryKey.of(
        RegistryKeys.DIMENSION_TYPE,
        Identifier(MODID, "farm_type")
    )


    lateinit var server: MinecraftServer
        private set


    val farmWorld: ServerWorld by lazy {
        server.getWorld(FARM_WORLD_KEY) ?: throw AssertionError("Lobby world does not exist")
    }

    val luckpermsApi by lazy {
        LuckPermsProvider.get()
    }


    override fun onInitializeServer() {
        Registry.register(Registries.CHUNK_GENERATOR, Identifier("textfarm", "void"), VoidWorldGenerator.CODEC)

        FARM_WORLD_KEY

        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            this.server = server
            luckpermsApi
            TextFarmDatabase.init()

            transaction {
                SchemaUtils.createMissingTablesAndColumns(Players, Islands)
            }
        }

        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            val player = handler.player

            transaction {
                val dbPlayer = player.dbPlayer
                if (dbPlayer == null) {
                    PlayerService.createNewPlayer(player)
                } else {
                    // Found
                    if (dbPlayer.name != player.name.literalString) {
                        PlayerService.updatePlayerName(player.uuid, player.name.literalString!!)
                    }
                }
                Unit
            }

        }


        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            if (!environment.dedicated) return@register
            dispatcher.register(FarmCommand)
        }

    }

}