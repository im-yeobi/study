package chapter02

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@InternalCoroutinesApi
fun main() {
    runBlocking {
        val task = GlobalScope.async {
            doSomething1()
        }

        // 예외를 전파하지 않는다.
        task.join()
        if (task.isCancelled) {
            // 예외를 가져온다.
            val exception = task.getCancellationException()
            println("Error with message : ${exception.cause}")
        } else {
            println("Success")
        }

        // 예외 전파
//        task.await()

        println("Completed")
    }
}

suspend fun doSomething1() {
    delay(100)
    println("tester")
//    throw UnsupportedOperationException()
}
