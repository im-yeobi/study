package chapter04

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    GlobalScope.launch(Dispatchers.Unconfined) {
        // main 스레드 실행
        println("Starting in ${java.lang.Thread.currentThread()}")
        delay(500)
        // Default Executor 스레드 실행
        println("Resuming in ${java.lang.Thread.currentThread()}")
    }.join()
}
