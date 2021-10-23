package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val job = GlobalScope.launch {
            delay(1000)
        }

        delay(2000)
        job.cancel()    // 잡 취소
        job.cancelAndJoin() // 잡 취소 & 취소가 완료될 때까지 현재 코루틴 일시 중단
    }
}
