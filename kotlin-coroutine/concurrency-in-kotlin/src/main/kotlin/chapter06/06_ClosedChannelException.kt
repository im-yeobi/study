package chapter06

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val channel = Channel<String>()
        channel.close()
        channel.send("hello")   // ClosedSendChannelException
        channel.isClosedForReceive
        channel.isEmpty
    }
}
