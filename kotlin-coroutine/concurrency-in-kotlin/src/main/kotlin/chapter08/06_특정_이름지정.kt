package chapter08

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    val pool = newFixedThreadPoolContext(3, "fixedPool")
    val ctx = newSingleThreadContext("singlePool")

    runBlocking {
        withContext(pool + CoroutineName("main")) {
            println("Running in ${Thread.currentThread().name}")

            withContext(ctx + CoroutineName("inner")) {
                println("Switching to ${Thread.currentThread().name}")
            }
        }
    }
}
