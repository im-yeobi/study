package chapter07

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val dispatcher = newFixedThreadPoolContext(3, "pool")
        val actor = actor<String>(dispatcher) {
            for (msg in channel) {
                println("Running in ${Thread.currentThread().name}")
            }
        }

        for (i in 1..10) {
            actor.send("a")
        }
        actor.close()
    }
}
