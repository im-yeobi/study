package chapter08

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class MockDataSource : DataSource {
    override fun getNameAsync(id: Int) = GlobalScope.async {
        delay(200)
        "Yeobi"
    }

    override fun getAgeAsync(id: Int) = GlobalScope.async {
        delay(500)
        LocalDate.now().year - 1982
    }

    override fun getProfessionAsync(id: Int) = GlobalScope.async {
        delay(2000)
        "Developer"
    }
}

fun main() {
    runBlocking {
        val userManager = UserManager(MockDataSource())
        val user = userManager.getUser(10)

        println(user.name == "Yeobi")
        println(user.age == LocalDate.now().year - 1982)
        println(user.profession == "Developer")
    }
}
