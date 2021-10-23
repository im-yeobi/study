package `01_coroutines_basic`

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {   // coroutine scope
        // main coroutine
        launch(Dispatchers.IO) {
            // launch coroutine
            delay(10000L)
            println("World!")
        }

//        println("Hello")
    }

//    println("end")
}
