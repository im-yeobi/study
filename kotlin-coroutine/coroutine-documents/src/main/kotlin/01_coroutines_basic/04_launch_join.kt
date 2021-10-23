package `01_coroutines_basic`

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        delay(10000000)
        println("World!")
    }

    println("Hello")
    job.join()  // launch 코루틴이 종료될 때까지 대기
    println("Done")
}
