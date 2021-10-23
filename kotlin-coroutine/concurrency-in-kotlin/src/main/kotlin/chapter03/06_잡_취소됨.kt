package chapter03

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val job = GlobalScope.launch {
            delay(5000)
        }

        delay(2000)

        // cancel
        job.cancel()

//        val cancellation = job.getCancellationException() // 코루틴 인터널 API
        println()
    }
}
