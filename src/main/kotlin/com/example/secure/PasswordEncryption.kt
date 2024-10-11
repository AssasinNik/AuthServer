package com.example.secure

import io.ktor.util.*
import de.mkammerer.argon2.Argon2Factory

private val argon2 = Argon2Factory.create()
fun hash(password :String?) : String{
    return argon2.hash(2, 65536, 1, password?.toCharArray())
}
fun verifyPassword(password: String, hash: String?): Boolean {
    return argon2.verify(hash, password.toCharArray())
}