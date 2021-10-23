package chapter04

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { context, throwable ->
        println("Error captured in $context")
        println("Message : ${throwable.message}")
    }

    GlobalScope.async(handler) {
        TODO("Not implemented yet")
    }.await()

    // wait for the error to happen
    delay(500)
}
