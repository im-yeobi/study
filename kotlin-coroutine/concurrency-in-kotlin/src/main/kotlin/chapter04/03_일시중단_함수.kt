package chapter04

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

data class SuspendProfile(
    val id: Long,
    val name: String,
    val age: Int
)

interface ProfileServiceClient {
    suspend fun fetchByName(name: String): SuspendProfile
    suspend fun fetchById(id: Long): SuspendProfile
}

class SuspendProfileServiceClient : ProfileServiceClient {
    override suspend fun fetchByName(name: String): SuspendProfile {
        return SuspendProfile(1, name , 28)
    }

    override suspend fun fetchById(id: Long): SuspendProfile {
        return SuspendProfile(id, "Susan" , 28)
    }
}

fun main() = runBlocking {
    val client = SuspendProfileServiceClient()
    val profile = client.fetchById(1)
    println(profile)
}
