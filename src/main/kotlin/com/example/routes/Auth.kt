package com.example.routes

import com.example.repository.UserRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import user.CreateUserParams
import user.LoginUserParams
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(repository: UserRepository){
    routing {
        route("/auth"){
            post("/register") {
                // Получаем тело запроса
                val requestBody = call.receiveText()
                log.info("Request body: $requestBody")  // Логируем тело запроса

                // Создаем объект Jackson для десериализации
                val mapper = jacksonObjectMapper()
                log.info("Mapper created")  // Логируем создание mapper

                // Преобразуем JSON в объект CreateUserParams вручную
                val params: CreateUserParams = mapper.readValue(requestBody)
                log.info("Parsed params: ${params.toString()}")  // Логируем полученные параметры

                // Обрабатываем запрос
                val result = repository.registerUser(params)
                log.info("Result from repository: $result")  // Логируем результат

                // Отправляем результат клиенту
                call.respond(result.statusCode, result)
            }
            post("/login"){
                val params =call.receive<LoginUserParams>()
                val result = repository.loginUser(params)
                call.respond(result.statusCode, result)
            }
        }
    }
}