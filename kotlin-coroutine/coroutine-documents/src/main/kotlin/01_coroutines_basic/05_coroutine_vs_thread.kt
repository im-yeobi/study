package `01_coroutines_basic`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main() {
//      코루틴 실행
        executeCoroutine()

//      스레드 실행
//        executeThread()
}

fun executeCoroutine() = runBlocking {
    repeat(100_000) {
        launch {
            delay(1000)
            println("Coroutine")
        }
    }
}

fun executeThread() {
    repeat(100_000) {
        thread {
            Thread.sleep(1000)
            println("Thread")
        }
    }
}
