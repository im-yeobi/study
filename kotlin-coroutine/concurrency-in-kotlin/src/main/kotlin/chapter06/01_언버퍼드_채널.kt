package chapter06

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
//    val channel = Rendez<Int>()
    runBlocking {
        val channelA = Channel<Int>()
        val channelB = Channel<Int>(0)

        val time = measureTimeMillis {
            val channel = Channel<Int>()
            val sender = GlobalScope.launch {
                repeat(10) {
                    channel.send(it)
                    println("Send $it")
                }
            }
            channel.receive()
            channel.receive()
            channel.receive()
        }
        println("Toook $time ms")
    }
}
