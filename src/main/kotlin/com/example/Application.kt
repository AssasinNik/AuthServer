package com.example

import com.example.data.DataBase
import com.example.plugins.*
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import com.example.routes.authRoutes
import service.UserService
import com.example.service.UserServiceImpl
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import java.io.File


fun main(args: Array<String>) {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DataBase.init()
    val service1: UserService = UserServiceImpl()
    val repository1: UserRepository = UserRepositoryImpl(service1)
    configureSerialization()
    configureRouting()
    authRoutes(repository1)
}
