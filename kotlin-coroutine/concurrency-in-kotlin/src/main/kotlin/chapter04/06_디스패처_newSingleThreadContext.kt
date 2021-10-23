package chapter04

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val dispatcher = newSingleThreadContext("newThread")

        GlobalScope.launch(dispatcher) {
            // newThread 스레드 실행
            println("Starting in ${java.lang.Thread.currentThread()}")
            delay(500)
            // newThread 스레드 실행
            println("Resuming in ${java.lang.Thread.currentThread()}")
        }.join()
    }
}
