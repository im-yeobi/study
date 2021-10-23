package chapter06

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val time = measureTimeMillis {
            val channel = Channel<Int>(Channel.CONFLATED)
            launch {
                repeat(5) {
                    channel.send(it)
                    println("Send $it")
                }
            }
            delay(500)
            val element = channel.receive()
            println("Received $element")
        }
        println("time : $time ms")
    }
}
