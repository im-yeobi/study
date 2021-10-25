package `04_coroutine_context_and_dispatchers`

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        launch(Dispatchers.Unconfined) {
            println("Unconfined start: ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined end: ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {
            println("Default start: ${Thread.currentThread().name}")
            delay(500)
            println("Default end: ${Thread.currentThread().name}")
        }
    }
}
