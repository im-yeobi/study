package chapter05

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.take
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val prod = produce {
            send(1)
            send("produce")
        }

        println(prod.receive())
        println(prod.receive())

        if (prod.isClosedForReceive)
            println(prod.receive())

//        printtake(5).consumeEach {  }ln(prod.receive())
    }
}
