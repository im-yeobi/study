package `02_cancellation_and_timeouts`

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        try {
            repeat(100) { i ->
                println("[job] $i")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("exception")
        } finally {
            println("canceled")
        }
    }

    delay(1000)
    println("[main] waiting")
    job.cancelAndJoin()
    println("[main] finished")
}
