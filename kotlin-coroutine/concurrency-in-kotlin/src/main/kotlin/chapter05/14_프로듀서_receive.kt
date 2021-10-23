package chapter05

import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        println(producer.receive())
        println(producer.receive())
    }
}
