import com.example.data.DataBase
import com.example.plugins.*
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
import org.junit.jupiter.api.Assertions
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
    override val primaryKey=PrimaryKey(TestTable.id)
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

        Assertions.assertEquals(1, count)
        transaction {
            SchemaUtils.drop(TestTable)
        }
    }
}