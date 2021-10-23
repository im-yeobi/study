package `02_cancellation_and_timeouts`

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

fun main() = runBlocking {
    val result = withTimeout(3000) {
        repeat(1000) { i ->
            println("[job] $i")
            delay(100)
        }
    }
    println(result)
}
