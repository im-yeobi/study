package `03_composing_suspending_functions`

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val time = measureTimeMillis {
            /* 순차
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
             */

            // 동시
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }

        println("Completed in $time ms")    // 순차: 대략 2000 ms 이상, 동시: 대략 1000ms
    }
}

@OptIn()
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    println("doSomethingUsefulOne()")
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    println("doSomethingUsefulTwo()")
    return 29
}
