package chapter02

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val task = launch {
            printCurrentThread()
        }

        val 디스패처_생성 = newSingleThreadContext(name = "ServiceCall")
        val 생성된_task = GlobalScope.launch(디스패처_생성) {
            printCurrentThread()
        }
        task.join()
    }
}

fun printCurrentThread() {
    println("Running in thread [${Thread.currentThread().name}]")
}
