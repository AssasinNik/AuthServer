package com.example.secure
fun generateRandomEmail(): String {
    val domains = listOf("gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com")
    val randomUsername = generateRandomString(8) // Генерируем случайное имя пользователя
    val randomDomain = domains.random() // Выбираем случайный домен из списка
    return "$randomUsername@$randomDomain"
}

fun generateRandomString(length: Int): String {
    val characters = ('a'..'z') + ('0'..'9') // Символы для имени пользователя
    return (1..length)
        .map { characters.random() }
        .joinToString("")
}