package chapter04

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun greetDelayed(delayMillis: Long) {
    delay(delayMillis)
    println("Hello, World!")
}

fun main() {
    runBlocking {
//        greetDelayed(1000)
        val retSuspendA = suspendA()
        val retSuspendB = suspendB()

        println("$retSuspendA, $retSuspendB")
    }
    println("after coroutine scope")
}

suspend fun suspendA(): String {
    delay(3000)
    return "suspendA"
}

suspend fun suspendB(): String {
    delay(3000)
    return "suspendA"

}
