package chapter07

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

var cnt = 0

//val atomicContext = CoroutineScope.

fun main() {
    runBlocking {
        val a = GlobalScope.launch { increase(5000) }
        val b = GlobalScope.launch { increase(5000) }
        val c = GlobalScope.launch { increase(5000) }

        a.join()
        b.join()
        c.join()
    }

    println("cnt : $cnt")
}

private suspend fun increase(repeatCnt: Int) {
    repeat(repeatCnt) { cnt++ }
}
