package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val deferred = GlobalScope.async {
            TODO("Not Implemented yet!")
        }

        // wait for it to fail
//        delay(2000)
        try {
            deferred.await()
        } catch (throwable: Throwable) {
            println("catch exception ${throwable.message}")
        }
    }
}
