package com.example.repository

import user.ChangePasswordParams
import user.CreateUserParams
import user.LoginUserParams
import com.example.utils.Response

interface UserRepository {
    suspend fun registerUser(params: CreateUserParams): Response<Any>
    suspend fun loginUser(params: LoginUserParams): Response<Any>
    suspend fun ChangePassword(params: ChangePasswordParams): Response<Any>
    suspend fun ChangeUsername(email: String, name: String, newUsername: String): Response<Any>
}