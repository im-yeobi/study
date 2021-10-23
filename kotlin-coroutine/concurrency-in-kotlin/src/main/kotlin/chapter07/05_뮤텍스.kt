package chapter07

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun main() {
    runBlocking {
        var mutex = Mutex()

        mutex.withLock {

        }
    }
}
