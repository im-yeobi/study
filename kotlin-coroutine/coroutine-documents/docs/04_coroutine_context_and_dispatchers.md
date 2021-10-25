## 코루틴 공식문서. [Coroutine context and dispatchers](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html)

- 코루틴은 `CoroutineContext` 로 표현되는 컨텍스트에서 실행된다. 
- 코루틴 컨텍스트는 다양한 요소들의 집합이다.(Set) 대표적인 요소는 Job이 있고 이번 섹션에서 dispatcher를 다룬다.

### Dispatchers and threads
- 코루틴 dispatcher는 코루틴 실행을 특정 스레드로 제한할 수 있있다. 스레드풀에서 실행하거나 제한하지 않을(unconfine) 수도 있다.  

```kotlin
launch { // context of the parent, main runBlocking coroutine
    println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
    println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher 
    println("Default               : I'm working in thread ${Thread.currentThread().name}")
}
launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
    println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
}
```

### Unconfined vs confined dispatcher
- Dispatchers.Unconfined
   - 코루틴을 호출한 스레드에서 코루틴을 실행시킨다. (첫 일시정지 전까지)
   - 일시정지된 코루틴이 재개될 때는 직전 코루틴을 호출했던 스레드에서 재개된다.
- default (Dispatcher 지정하지 않음) 
   - outer 코루틴 스코프를 상속한다. Default 디스패처는 호출한 스레드로 제한된다.
   - 실행 스레드가 제한되기 때문에 FIFO 스케줄링을 예상할 수 있다.

### Debugging coroutines and threads
- 로그를 위해 `-Dkotlinx.coroutines.debug` 옵션 사용
- `newSingleThreadContext`가 더이상 필요하지 않을 때 스레드를 해지하기 위해 use(코틀린 표준 라이브러리) 사용  

```kotlin
newSingleThreadContext("Ctx1").use { ctx1 ->
    newSingleThreadContext("Ctx2").use { ctx2 ->
        runBlocking(ctx1) {
            log("Started in ctx1")
            withContext(ctx2) {
                log("Working in ctx2")
            }
            log("Back to ctx1")
        }
    }
}
```

### Children of a coroutine
- 부모 코루틴이 취소되면 자식 코루틴까지 취소된다. (라이프 사이클이 동일하다)
- 예외 상황
   - 부모 코루틴과 자식 코루틴의 스코프가 다를 때 부모 스코프의 Job을 상속하지 않는다.
   - 새로운 코루틴에 다른 Job 객체를 컨텍스트로 전달하면 부모 스코프의 Job을 override 한다.

```kotlin
runBlocking {
    launch {
        launch(Job()) {
          // 새로운 Job 컨텍스트로 동작한다.  
          println("job1: I run in my own Job and execute independently!")
          delay(1000)
          println("job1: I am not affected by cancellation of the request")
        }
      
        launch {
          // 부모 컨텍스트를 상속한다
          delay(100)
          println("job2: I am a child of the request coroutine")
          delay(1000)
          println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // 부모 컨텍스트를 상속한 Job만 실행이 취소된다.
}
```

### Parental responsibilities
- 부모 코루틴은 자식 코루틴이 종료될 때까지 기다린다.
```kotlin
runBlocking {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        repeat(3) { i -> // launch a few children jobs
            launch() {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // join() 이 없더라도 부모 코루틴은 자식 코루틴이 시행 완료될 때까지 기다린다.
    println("Now processing of the request is complete")
}
```

### 코루틴 이름 지정
- 코루틴 네이밍은 자동적으로 id가 지정되지만, 필요한 경우 이름을 직접 지정할 수 있다.
- 디버깅이 필요할 때 코루틴별로 네이밍을 지정하면 좋을 것 같다.

```kotlin
async(CoroutineName("v1coroutine")) {
    // ...
}
```

### 컨텍스트 요소 결합
- `+` 연산자를 이용해 여러 요소를 결합해서 코루틴 컨텍스트를 만들 수 있다.
```kotlin
launch(Dispatchers.Default + CoroutineName("myCoroutine")) {
    // ...
}
```


