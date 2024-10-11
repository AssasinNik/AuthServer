package com.example.repository


import service.UserService
import user.ChangePasswordParams
import user.CreateUserParams
import user.LoginUserParams

import com.example.utils.Response

class UserRepositoryImpl(private val userService: UserService) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): Response<Any>{
        val user=userService.registerUser(params)
        if(user != null){
            return Response.SuccessResponse(data = user)
        }else{
            return Response.ErrorResponse()
        }
    }
    override suspend fun loginUser(params: LoginUserParams): Response<Any> {
        val user= userService.findUser(params)
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