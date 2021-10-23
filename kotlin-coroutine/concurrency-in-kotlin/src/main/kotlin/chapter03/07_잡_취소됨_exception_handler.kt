package chapter03

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

fun main() {
    runBlocking {
        val exceptionHandler = CoroutineExceptionHandler {  // 잡에서 예외 발생한 경우에만 에러 전파
            _: CoroutineContext, throwable: Throwable ->
                println("Job cancelled due to ${throwable.message}")
        }

        val job = GlobalScope.launch(exceptionHandler) {
//            TODO("Not Implemented")
            println("잡")
        }
        job.cancelAndJoin()

        delay(2000)
    }
}
