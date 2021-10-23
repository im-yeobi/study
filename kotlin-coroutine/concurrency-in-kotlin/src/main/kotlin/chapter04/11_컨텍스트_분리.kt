package chapter04

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val dispatcher = newSingleThreadContext("myDispatcher")
    val handler = CoroutineExceptionHandler { _, throwable ->
        println("Error captured")
        println("Message: ${throwable.message}")
    }

    val context = dispatcher + handler
    val tmpContext = context.minusKey(dispatcher.key)   // 컨텍스트에서 디스패처 요소 제거

    GlobalScope.launch(tmpContext) {
        println("Running in ${java.lang.Thread.currentThread().name}")
        TODO("Not impemented")
    }.join()
}
