package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val headlinesTask = launch {
            delay(1000)
            println("test")
            "return value"
        }

//        headlinesTask.await()

        // val articlesTask = CompletableDeferred<List<Article>>()
    }
}
