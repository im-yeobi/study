package chapter04

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

data class AsyncProfile(
    val id: Long,
    val name: String,
    val age: Int
)

class AsyncProfileServiceClient {
    fun asyncFetchByName(name: String) = GlobalScope.async {
        AsyncProfile(1, name , 28)
    }

    fun asyncFetchById(id: Long) = GlobalScope.async {
        AsyncProfile(id, "Susan" , 28)
    }
}

fun main() = runBlocking {
    val client = AsyncProfileServiceClient()
    val profile = client.asyncFetchById(1).await()
    println(profile)
}
