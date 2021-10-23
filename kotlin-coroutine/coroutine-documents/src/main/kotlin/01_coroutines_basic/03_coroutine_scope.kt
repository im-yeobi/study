package `01_coroutines_basic`

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    doWorld2()
}

suspend fun doWorld2() = coroutineScope {
    launch {
        delay(1000)
        println("World!")
    }
    println("Hello")
}
