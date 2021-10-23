## 코루틴 공식문서. [Coroutines basics](https://kotlinlang.org/docs/coroutines-basics.html#your-first-coroutine)

### 공부를 통해 얻고자 하는 것
- 코루틴이 무엇인가?
- 코루틴과 스레드의 차이점은?
- blocking 과 non-blocking의 차이점은?

### Your first coroutine
- 코루틴은 일시 정지 가능하다. 개념적으로 코드를 동시에 실행해야 한다는 의미에서 스레드와 비슷하다. 
- 하지만 코루틴의 동작을 특정 스레드로 한정하지 않는다. 코루틴은 특정 스레드에서 실행이 중단되고 다른 스레드에서 재개될 수 있다. 
- 코루틴을 경량 스레드라고 볼 수 있지만, 실제로 많은 차이가 있다. (**어떤 차이점이 있는지 이해하는 것이 중요**)

```kotlin
fun main() = runBlocking {   // coroutine scope. blocking
    // main coroutine
    launch {
        // launch coroutine
        delay(1000L)    // suspending function. non-blocking
        println("World!")
    }

    println("Hello")
}
```

### Structured concurrency
- 코루틴은 Coroutine Scope 안에서만 실행 가능하다.
- 부모 Scope는 자식 코루틴이 종료될 때까지 수행을 완료할 수 없다.

### Suspending function
```kotlin
fun main() = runBlocking {
    launch { doWorld() }
    println("Hello")
}

suspend fun doWorld() {
    delay(1000) // suspend function 안에서 코루틴 빌더 실행
    println("World!")
}
```

### Scope builder
- 코루틴 Scope를 생성하는 빌더
- `runBlocking`, `coroutineScope` 
   - 공통점 : `runBlocking`, `coroutineScope` 모두 자식 코루틴이 종료될 때까지 대기한다.
   - 차이점 : 
      - `runBlocking`은 현재 스레드를 block 시킨다. regular function 
      - `coroutineScope`는 일시 정지 시킨다. suspending function

### Job
- `launch` 코루틴 빌더는 `Job`을 반환한다. `join()` 메소드로 코루틴 실행이 완료될 때까지 기다린다.
```kotlin
fun main() = runBlocking {
    val job = launch {
        delay(1000)
        println("World!")
    }

    println("Hello")
    job.join()  // launch 코루틴이 종료될 때까지 대기
    println("Done")
}
```

### light-weight
```kotlin
fun executeCoroutine() = runBlocking {
    repeat(10_000) {
        launch {
            delay(1000)
            println("Coroutine")
        }
    }
}

fun executeThread() {
    repeat(10_000) {
        thread {
            Thread.sleep(1000)
            println("Thread")
        }
  }
}
```

### 정리
- 코루틴이 무엇인가?
   - 코루틴은 코드를 동시에 처리하기 위한 개념이다.
   - 일시정지, 재개를 반복하며 여러 코루틴이 동시에 실행된다.
- 코루틴과 스레드의 차이점은?
   - 코투린의 실행은 특정 스레드에 국한되지 않는다. 코루틴 실행은 일시정지 되었다가 다른 스레드에서 재개될 수 있다.
- blocking 과 non-blocking의 차이점은? [IBM asynchronous I/O](https://developer.ibm.com/articles/l-async/)
   - blocking(제어권) : 호출된 함수가 작업이 완료될 때까지 제어권을 가짐. 
   - non-blocking(제어권) :  호출된 함수가 작업이 완료되지 않았더라도 제어권을 바로 반환해, 호출한 곳에서 수행을 이어감.
   - sync(동시성) :  호출된 함수의 수행 결과를 호출한 곳에서 처리.
   - async(동시성) : 호출된 함수의 수행 결과를 호출된 곳에서만 처리. (호출한 곳에서는 알지 못함)
- 동시성(Concurrency)과 병렬성(Parallelism) [Coroutine, Thread 와의 차이점](https://aaronryu.github.io/2019/05/27/coroutine-and-thread/)
   - 병렬은 동시성을 의미하지만 동시성은 병렬성이 없이도 발생할 수 있다. 
   - 동시성 : 동시성은 정확히 같은 시점에 실행되는지 여부와는 상관이 없다.
   - 병렬성 : 병렬 실행은 두 스레드가 정확히 같은 시점에 실행될 때만 발생한다. (두 개 이상의 스레드가 필요하다)
