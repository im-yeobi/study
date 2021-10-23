package chapter07

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

var actorCnt = 0
val actorContext = newSingleThreadContext("actor-context")
val actorCounter = GlobalScope.actor<Void?>(actorContext) {
    for (msg in channel)
        ++actorCnt
}

fun main() {
    runBlocking {
        val jobA = asyncIncrease(100)
        val jobB = asyncIncrease(100)

        jobA.join()
        jobB.join()
    }

    println("cnt : $actorCnt")
}

private suspend fun asyncIncrease(repeatCnt: Int) =
    GlobalScope.launch(actorContext) {
        repeat(repeatCnt) {
            actorCounter.send(null)
        }
    }
