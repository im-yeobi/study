package `02_cancellation_and_timeouts`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("[job] $i")
            delay(500)
        }
    }

    delay(1000)
    println("[main] waiting")
    job.cancel()
    job.join()
    println("[main] finished")
}
