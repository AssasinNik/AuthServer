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

object TestTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(TestTable.id)
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

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "Илья", "email": "ilya1.bystrikov19@gmail.com", "password": "Parol1810!"}""")
        }

        // Проверка кода состояния
        assertEquals(HttpStatusCode.OK, response.status)

        // Проверка формата ответа
        val expectedJson = """
            {
              "exception" : null,
              "message" : null,
              "statusCode" : {
                "value" : 200,
                "description" : "OK"
              }
            }
        """.trimIndent()
        assertEquals(expectedJson, response.bodyAsText())
    }
}
