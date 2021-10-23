package `02_cancellation_and_timeouts`

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // isActive
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("[job] ${i++}")
                nextPrintTime += 500L
            }
        }
    }

    delay(1000)
    println("[main] waiting")
    job.cancelAndJoin()
    println("[main] finished")
}
