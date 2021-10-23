package `01_coroutines_basic`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        launch {
//            doWorld()
            delay(1000)
            println("World!")
        }
        println("Hello")
    }
}

suspend fun doWorld() {
    delay(1000)
    println("World!")
}
