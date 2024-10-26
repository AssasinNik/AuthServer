package com.example.service

import com.example.data.DataBase.dbQuery
import com.example.secure.hash
import user.ChangePasswordParams
import user.CreateUserParams
import user.LoginUserParams
import com.example.user.UserDTO
import com.example.user.Users
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import service.UserService
import java.util.concurrent.CompletableFuture


class UserServiceImpl : UserService {
    override fun registerUser(params: CreateUserParams): CompletableFuture<UserDTO?> {
        return CompletableFuture.supplyAsync {
            runBlocking {
                asyncOperationRegister(params)
            }
        }
    }
    private suspend fun asyncOperationRegister(params: CreateUserParams): UserDTO? {
        var statement: InsertStatement<Number>? = null
        var flag: UserDTO?
        runBlocking { flag=asyncOperationFind(params.email,params.password) }
        if (flag != null){
            dbQuery {
                statement = Users.insert {
                    it[email] = params.email
                    it[parol_user] = params.password
                    it[username] = params.username
                }
            }
            return allrowToUser(statement?.resultedValues?.get(0))
        }else{
            return null
        }
    }
    override fun findUser(params: LoginUserParams): CompletableFuture<UserDTO?>{
        return CompletableFuture.supplyAsync {
            runBlocking {
                asyncOperationFind(params.getEmail(), params.getParol_user())
            }
        }
    }
    private suspend fun asyncOperationFind(email: String, password: String): UserDTO? {
        return dbQuery {
            Users.select { (Users.email eq email) and (Users.parol_user eq password) }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
        }
    }

    override fun changeUsername(email: String, name: String, newUsername: String): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            runBlocking {
               asyncOperationChangeUN(email, name, newUsername)
            }
        }
    }
    private suspend fun asyncOperationChangeUN(email: String, name: String, newUsername: String): Boolean {
        return dbQuery {
            val user = Users.select {
                (Users.email eq email) and (Users.username eq name)
            }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
            if (user!=null){
                val updatedRows = Users.update({ Users.username eq name }) {
                    it[username] = newUsername
                }
                updatedRows > 0
            }
            else{
                false
            }
        }
    }

    override fun changePassword(params: ChangePasswordParams): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            runBlocking {
                asyncOperationChangePs(params.email, params.old_parol_user, params.new_parol_user)
            }
        }
    }

    private suspend fun asyncOperationChangePs(email: String, password: String, newPassword: String): Boolean {
        return dbQuery {
            val user = Users.select {
                (Users.email eq email) and (Users.parol_user eq password)
            }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()

            if (user != null) {
                val updatedRows = Users.update({ Users.email eq email }) {
                    it[parol_user] = newPassword
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
