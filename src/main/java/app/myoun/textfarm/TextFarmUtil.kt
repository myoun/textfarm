package app.myoun.textfarm

import app.myoun.textfarm.TextFarmServer.luckpermsApi
import app.myoun.textfarm.data.dao.Player
import app.myoun.textfarm.data.service.PlayerService
import net.luckperms.api.model.user.User
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.border.WorldBorder

object TextFarmUtil {

    fun setPlayerBorder(player: ServerPlayerEntity, center: Pair<Double, Double>, size: Double) {
        val border = WorldBorder().also {
            it.size = size
            it.setCenter(center.first, center.second)
        }
        val packet = WorldBorderInitializeS2CPacket(border)
        player.networkHandler.sendPacket(packet)
    }

    val PlayerEntity.luckpermsUser: User?
        get() = luckpermsApi.userManager.getUser(this.uuid)

    val PlayerEntity.dbPlayer: Player?
        get() = PlayerService.getPlayer(this.uuid)

}