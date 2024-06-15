package app.myoun.textfarm.data.dao

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.*

object Players : IdTable<UUID>("players") {
    override val id = uuid("uuid").entityId()
    val name = varchar("name", 16)
    val dateCreated = datetime("date_created").defaultExpression(CurrentDateTime)
    val island = reference("island_id", Islands).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class Player(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, Player>(Players)

    var name: String by Players.name
    var dateCreated: LocalDateTime by Players.dateCreated
    val island: Island? by Island optionalReferencedOn Players.island
}