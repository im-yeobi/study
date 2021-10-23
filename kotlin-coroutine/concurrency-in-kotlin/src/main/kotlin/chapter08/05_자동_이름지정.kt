package chapter08

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    val pool = newFixedThreadPoolContext(3, "fixed pool")
    val ctx = newSingleThreadContext("single ctx")

    runBlocking {
        val tasks = mutableListOf<Deferred<Unit>>()
        for (i in 0..5) {
            val task = GlobalScope.async(pool) {
                println("Processing $i in ${Thread.currentThread().name}")

                withContext(ctx) {
                    println("Step two of $i happening in thread ${Thread.currentThread().name}")
                }

                println("Finishing $i in ${Thread.currentThread().name}")
            }

            tasks.add(task)
        }

        for (task in tasks) task.await()
    }
}
