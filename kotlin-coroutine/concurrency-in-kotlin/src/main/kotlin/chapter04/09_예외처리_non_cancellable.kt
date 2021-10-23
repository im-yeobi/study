package chapter04

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val duration = measureTimeMillis {
        val job = launch {
            try {
                while (isActive) {
                    delay(500)

                    println("still running")
                }
            } finally {
                println("cancelled, will delay finalization now")
                delay(5000) // 실제로 동작하지 않음. 취소 중인 코루틴은 일시 중단될 수 없도록 설계됐다.
                println("delay completed, bye")
            }
        }

        delay(1200)
        job.cancelAndJoin()
    }

    println("Took $duration ms")
}
