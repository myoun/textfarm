package app.myoun.textfarm.data.dao

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime


object Islands : IntIdTable("islands") {
    val name = varchar("name", 32)
    val dateCreated = datetime("date_created").defaultExpression(CurrentDateTime)
    val owner = reference("owner", Players).uniqueIndex()
}

class Island(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Island>(Islands)

    var name: String by Islands.name
    var dateCreated: LocalDateTime by Islands.dateCreated
    var owner: Player by Player referencedOn Islands.owner
}
