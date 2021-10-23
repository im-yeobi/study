package chapter06

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.take
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
//    val channel = Channel<Int>(50)
//    val arrayChannel = ArrayChannel<Int>()

    runBlocking {
        val time = measureTimeMillis {
            val channel = Channel<Int>(1)
            val sender = GlobalScope.launch {
                repeat(10) {
                    channel.send(it)
                    println("send $it")
                }
            }
            delay(500)
            println("Taking two")
            channel.receive()
            channel.receive()
            delay(500)
        }
        println("time : $time ms")
    }
}
