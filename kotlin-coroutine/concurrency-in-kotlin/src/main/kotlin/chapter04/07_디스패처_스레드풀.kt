package chapter04

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val dispatcher = newFixedThreadPoolContext(4, "myPool")

        GlobalScope.launch(dispatcher) {
            // myPool-1 스레드 실행
            println("Starting in ${Thread.currentThread()}")
            delay(500)
            // myPool-2 스레드 실행
            println("Resuming in ${Thread.currentThread()}")
        }.join()
    }
}
