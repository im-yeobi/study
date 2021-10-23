package chapter05

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

val context = newSingleThreadContext("myThread")

val producer = GlobalScope.produce(context) {
    for (i in 0..9)
        send(i)
}

fun main() {
    runBlocking {
//        val producer = produce {
//            send(1)
//        }
//
//        val producerT: ReceiveChannel<Any> = produce {
//            send(1)
//            send(10L)
//            send("produce")
//        }
    }
}
