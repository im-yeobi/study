package chapter07

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking

@ObsoleteCoroutinesApi
fun main() {
    runBlocking {
        val bufferedPrinter = actor<String>(capacity = 10) {
            for (msg in channel) {
                println(msg)
            }
        }

        bufferedPrinter.send("hello")
        bufferedPrinter.send("world")

        bufferedPrinter.close()
    }
}
