import com.example.data.DataBase
import com.example.plugins.*
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import com.example.routes.authRoutes
import com.example.service.UserServiceImpl
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import service.UserService
import kotlin.test.*

object TestTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(TestTable.id)
}

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("meow Auth", bodyAsText())
        }
    }
}

class DatabaseTests {

    @Test
    fun testInsertData() = runBlocking {
        DataBase.init()
        transaction {
            SchemaUtils.create(TestTable)
        }

        DataBase.dbQuery {
            TestTable.insert {
                it[name] = "Test"
            }
        }

        val count = DataBase.dbQuery {
            TestTable.select { TestTable.name eq "Test" }.count()
        }

        assertEquals(1, count)

        transaction {
            SchemaUtils.drop(TestTable)
        }
    }
}

class PostRequestTest {


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

    @Test
    fun testPostRequest() = testApplication {
        application {
            DataBase.init()
            val service: UserService = UserServiceImpl()
            val repository: UserRepository = UserRepositoryImpl(service)
            configureSerialization()
            configureRouting()
            authRoutes(repository)
        }

        val email = generateRandomEmail()
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "Илья", "email": "${email}", "password": "Parol1810!"}""")
        }

        // Проверка кода состояния
        assertEquals(HttpStatusCode.OK, response.status)

        assertTrue(response.bodyAsText().contains("$email"))
    }

    @Test
    fun testPostRequestInvalidEmail() = testApplication {
        application {
            DataBase.init()
            val service: UserService = UserServiceImpl()
            val repository: UserRepository = UserRepositoryImpl(service)
            configureSerialization()
            configureRouting()
            authRoutes(repository)
        }

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "Илья", "email": "invalid-email", "password": "Parol1810!"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Invalid email format"))
    }

    @Test
    fun testPostRequestEmptyFields() = testApplication {application {
        DataBase.init()
        val service: UserService = UserServiceImpl()
        val repository: UserRepository = UserRepositoryImpl(service)
        configureSerialization()
        configureRouting()
        authRoutes(repository)
    }

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "", "email": "", "password": ""}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Username, email, and password cannot be empty"))
    }

    @Test
    fun testPostRequestNotFound() = testApplication {
        application {
            DataBase.init()
            val service: UserService = UserServiceImpl()
            val repository: UserRepository = UserRepositoryImpl(service)
            configureSerialization()
            configureRouting()
            authRoutes(repository)
        }

        // Запрос на несуществующий маршрут
        val response = client.get("/auth/nonexistentRoute")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testPostRequestUserAlreadyRegistered() = testApplication {
        application {
            DataBase.init()
            val service: UserService = UserServiceImpl()
            val repository: UserRepository = UserRepositoryImpl(service)
            configureSerialization()
            configureRouting()
            authRoutes(repository)
        }


        val email = generateRandomEmail()

        // Регистрируем первого пользователя
        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "Илья", "email": "${email}", "password": "Parol1810!"}""")
        }

        // Пытаемся зарегистрировать второго пользователя с тем же email
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "Алексей", "email":  "${email}", "password": "AnotherPassword123!"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Email already in use"))
    }
}
