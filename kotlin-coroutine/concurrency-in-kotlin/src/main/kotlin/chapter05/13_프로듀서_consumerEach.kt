package chapter05

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        producer.consumeEach {
            println(it)
        }
    }
}
