package chapter02

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val task = GlobalScope.launch {
        doSomething2()
    }

    task.join()
    println("Completed")
}

fun doSomething2() {
    throw UnsupportedOperationException()
}
