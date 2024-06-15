package app.myoun.textfarm.data.service

import app.myoun.textfarm.data.dao.Player
import net.minecraft.entity.player.PlayerEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object PlayerService {

    fun createNewPlayer(player: PlayerEntity): Player {
        return transaction {
            Player.new(player.uuid) {
                name = player.name.literalString!!
            }
        }
    }

    fun updatePlayerName(uuid: UUID, name: String): Player? {
        return transaction {
            Player.findByIdAndUpdate(uuid) {
                it.name = name
            }
        }
    }

    fun getPlayer(uuid: UUID): Player? {
        return transaction {
            Player.findById(uuid)
        }
    }

}


