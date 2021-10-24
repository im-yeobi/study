package `03_composing_suspending_functions`

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        suspendSomethingUsefulOneAsync()
        suspendSomethingUsefulTwoAsync()
    }
}


@OptIn(DelicateCoroutinesApi::class)
suspend fun suspendSomethingUsefulOneAsync() = coroutineScope {
//    throw IllegalArgumentException()
    doSomethingUsefulOne()
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun suspendSomethingUsefulTwoAsync() = coroutineScope {
    doSomethingUsefulTwo()
}
