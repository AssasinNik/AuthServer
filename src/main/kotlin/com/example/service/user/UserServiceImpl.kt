package com.example.service.user

import com.example.data.DataBase.dbQuery
import com.example.secure.hash
import com.example.user.UserDTO
import com.example.user.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserServiceImpl : UserService {

    override suspend fun registerUser(params: CreateUserParams): UserDTO? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Users.insert {
                it[email] = params.email
                it[parol_user] = hash(params.password)
                it[username] = params.username
            }
        }
        return allrowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUser(email: String, password: String): UserDTO? {
        return dbQuery {
            Users.select { (Users.email eq email) and (Users.parol_user eq hash(password)) }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun changeUsername(name: String, newUsername: String): Boolean {
        return dbQuery {
            val updatedRows = Users.update({ Users.username eq name }) {
                it[username] = newUsername
            }
            updatedRows > 0
        }
    }

    override suspend fun changePassword(email: String, password: String, newPassword: String): Boolean {
        return dbQuery {
            val user = Users.select {
                (Users.email eq email) and (Users.parol_user eq hash(password))
            }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()

            if (user != null) {
                val updatedRows = Users.update({ Users.email eq email }) {
                    it[parol_user] = hash(newPassword)
                }
                updatedRows > 0
            } else {
                false
            }
        }
    }

    private fun rowToUser(row: ResultRow?): UserDTO? {
        return if (row == null) {
            null
        } else UserDTO(
            id = row[Users.id],
            email = row[Users.email],
            username = row[Users.username]
        )
    }

    private fun allrowToUser(row: ResultRow?): UserDTO? {
        return if (row == null) {
            null
        } else UserDTO(
            id = row[Users.id],
            email = row[Users.email],
            username = row[Users.username],
            parol = row[Users.parol_user]
        )
    }
}
