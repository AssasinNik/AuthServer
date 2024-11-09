package com.example.repository


import service.UserService
import user.ChangePasswordParams
import user.CreateUserParams
import user.LoginUserParams

import com.example.utils.Response
import kotlinx.coroutines.future.await

class UserRepositoryImpl(private val userService: UserService) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): Response<Any>{
        if(params.email=="" || params.password=="" || params.username==""){
            return Response.ErrorResponse(message = "Username, email, and password cannot be empty")
        }
        if (!params.email.contains("@") || !params.email.contains(".com")){
            return Response.ErrorResponse(message = "Invalid email format")
        }
        val user=userService.registerUser(params).await()
        return if(user != null){
            Response.SuccessResponse(data = user)
        }else{
            Response.ErrorResponse(message = "Email already in use")
        }
    }
    override suspend fun loginUser(params: LoginUserParams): Response<Any> {
        val user= userService.findUser(params).await()
        if(user!=null){
            return Response.SuccessResponse(data = user)
        }
        else{
            return Response.ErrorResponse(message = "Invalid password")
        }
    }

    override suspend fun ChangePassword(params: ChangePasswordParams): Response<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun ChangeUsername(email: String, name: String, newUsername: String): Response<Any> {
        TODO("Not yet implemented")
    }
}