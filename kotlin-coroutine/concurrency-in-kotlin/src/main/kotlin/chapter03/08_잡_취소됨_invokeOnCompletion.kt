package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking<Unit> {
        GlobalScope.launch {
            TODO("Not Implemented")
        }.invokeOnCompletion { cause ->
            cause?.let {
                println("Job cancelled due to ${cause.message}")
            }
        }
    }
}
