package com.example.user

import org.jetbrains.exposed.sql.Table

object Users: Table("users") {
    val id = Users.integer("id").autoIncrement()
    val username = Users.varchar("username", 40)
    val email= Users.varchar("email", 320)
    val parol_user=Users.varchar("parol_user", 200)
    val image=Users.varchar("image", 150)
    override val primaryKey=PrimaryKey(id)
}
