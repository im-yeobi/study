package `02_cancellation_and_timeouts`

import kotlinx.coroutines.*

var acquired = 0

class Resource {
    init { acquired++ }
    fun close() { acquired-- }
}

fun main() {
    runBlocking {
        repeat(10_000) { i -> // 100,000 코루틴
            launch {
                // launch 코루틴 안에서 TimeoutCancellationException 발생
                val resource = withTimeout(25) {    // Timeout 30 ms
                    delay(1)
                    Resource()
                }
                resource.close()    // release Resource
            }
        }
    }

    // runBlocking 완료
    println(acquired)
}
