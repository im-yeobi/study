package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val job = GlobalScope.launch {
            // 백그라운드 작업 수행
            // fire and forget
        }
    }
}
