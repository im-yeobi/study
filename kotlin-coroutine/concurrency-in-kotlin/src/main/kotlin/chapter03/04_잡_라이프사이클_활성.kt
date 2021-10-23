package chapter03

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val job = GlobalScope.launch {
            TODO("Not Implemented")
        }

        // 잡 완료될 때까지 기다리지 않는다.
        job.start()

        // 잡 완료될 때까지 기다린다.
//        job.join()
    }
}
