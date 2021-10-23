package chapter03

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        GlobalScope.launch(start = CoroutineStart.LAZY) {
            // LAZY : 자동 시작되지 않음
            TODO("Not Implemented")
        }

        delay(1000)
    }
}
