package chapter08

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDate

data class User(
    val name: String,
    val age: Int,
    val profession: String
)

interface DataSource {
    fun getNameAsync(id: Int): Deferred<String>
    fun getAgeAsync(id: Int): Deferred<Int>
    fun getProfessionAsync(id: Int): Deferred<String>
}

class UserManager(private val dataSource: DataSource) {
    suspend fun getUser(id: Int): User {
        val name = dataSource.getNameAsync(id)
        val age = dataSource.getAgeAsync(id)
        val profession = dataSource.getProfessionAsync(id)

        // 직업 조회가 오래 걸리기 때문에 직업 조회만 await()
        profession.await()

        return User(
            name.getCompleted(),
            age.getCompleted(),
            profession.getCompleted()
        )
    }
}

