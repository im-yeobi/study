package chapter06

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val channel = Channel<String>()
        println(channel.isClosedForSend)
        channel.close()
        println(channel.isClosedForSend)
    }
}
