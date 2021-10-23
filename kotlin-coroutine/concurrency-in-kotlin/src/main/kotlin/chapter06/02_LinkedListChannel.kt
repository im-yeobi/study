package chapter06

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val time = measureTimeMillis {
            val channel = Channel<Int>(Channel.UNLIMITED)
            val sender = GlobalScope.launch {
                repeat(5) {
                    println("send $it")
                    channel.send(it)
                }
            }
            channel.receive()
            channel.receive()
        }
        println("time : $time ms")
    }
}
