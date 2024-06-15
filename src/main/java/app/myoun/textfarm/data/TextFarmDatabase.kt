package app.myoun.textfarm.data

import app.myoun.textfarm.TextFarmServer
import org.jetbrains.exposed.sql.Database

object TextFarmDatabase {

    val db by lazy {
        val path = TextFarmServer.server.getFile("data/textfarm.db")
        if (!path.parentFile.exists()) {
            path.parentFile.mkdirs()
        }
        print(path)
        Database.connect("jdbc:sqlite:${path}")
    }

    fun init() {
        db
    }




}