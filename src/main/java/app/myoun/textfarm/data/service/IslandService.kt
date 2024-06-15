package app.myoun.textfarm.data.service

import app.myoun.textfarm.data.dao.Island
import app.myoun.textfarm.data.dao.Player
import org.jetbrains.exposed.sql.transactions.transaction

object IslandService {

    fun createNewIsland(player: Player): Island {
        return transaction {
            Island.new {
                owner = player
                name = "${player.name}의 섬"
            }
        }
    }

    fun playerHasIsland(player: Player): Boolean {
        return transaction { player.island != null }
    }

    fun getIslandCoordinate(player: Player): Pair<Int, Int> {
        return transaction {
            try {
                requireNotNull(player.island)

                val islandValue = player.island!!.id.value
                Pair(islandValue * 1000, islandValue * 1000)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Pair(0,0)
            }

        }
    }
}