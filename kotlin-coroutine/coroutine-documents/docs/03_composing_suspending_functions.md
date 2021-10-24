## 코루틴 공식문서. [Composing suspending functions](https://kotlinlang.org/docs/composing-suspending-functions.html)

### Sequential by default
- suspend 함수는 기본적으로 순차적으로 수행된다. 

```kotlin
val time = measureTimeMillis {
    val one = doSomethingUsefulOne()
    val two = doSomethingUsefulTwo()
    println("The answer is ${one + two}")
}
println("Completed in $time ms")
```

````text
The answer is 42
Completed in 2017 ms
````

### async 사용한 동시 처리
- `doSomethingUsefulOne`과 `doSomethingUsefulTwo` 간에 호출 의존관계가 없다면 동시 처리로 더 빠르게 결과를 얻을 수 있다.
- `launch`는 Job을 반환하지만 결과를 전달하지 않는다.
- `async`는 Deferred를 반환하고 결과를 전달한다.

```kotlin
val time = measureTimeMillis {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

```koltin
The answer is 42
Completed in 1017 ms
```

### Lazy 실행
- Lazy 모드로 지정하면 코루틴이 곧바로 실행되지 않고 `start`, `await`가 호출될 때 실행된다.
```kotlin
val time = measureTimeMillis {
    val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
    val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
    // some computation
    one.start() // start the first one
    two.start() // start the second one
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

```text
The answer is 42
Completed in 1017 ms
```

- `start` 없이 `await`만 하게 되면 위 예제와 동일하게 순차적으로 수행된다.
```kotlin
val time = measureTimeMillis {
    val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
    val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
    
    // 순차 실행
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

```text
The answer is 42
Completed in 2013 ms
```

### Async function
- GlobalScope로 async 함수를 정의할 수 있다. 해당 함수는 항상 비동기로 실행된다.
- 외부에서 async 함수를 사용할 때 코루틴 스코프가 아니어도 호출할 수 있지만, 결과르 얻기 위해서는 `runBlocking`을 사용해야 한다. 이때 스레드를 block 하게 된다.

```kotlin
@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}

// note that we don't have `runBlocking` to the right of `main` in this example
fun main() {
    val time = measureTimeMillis {
        // we can initiate async actions outside of a coroutine
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        // but waiting for a result must involve either suspending or blocking.
        // here we use `runBlocking { ... }` to block the main thread while waiting for the result
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time ms")
}

```

### Structured concurrency with asycn
- 기본적으로 코루틴 스코프는 자식 스코프가 완료될 때까지 대기하는데 자식 스코프에서 에러가 발생하면 실행이 종료된다.
- 예제에서는 두 번째 코루틴에서 에러가 발생했는데, 첫 번째 코루틴으로 에러가 전파되어 코루틴 실행이 취소된다.

```kotlin
fun main() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> { 
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> { 
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}
```

```text
Second child throws an exception
First child was cancelled
Computation failed with ArithmeticException
```
