## Chapter 04. 일시 중단 함수와 코루틴 컨텍스트
- 일시 중단 함수(suspending functions)를 배워보고 지금까지 사용했던 비동기 함수와 비교해본다.
- 코루틴 컨텍스트와 그 사용법도 자세히 다룬다.

### 일시 중단 함수
- 일시 중단 함수를 만드려면 시그니처에 suspend 제어자만 추가하면 된다.
- 일시 중단 함수는 `delay()`와 같은 일시 중단 함수를 직접 호출할 수 있다.
```kotlin
suspend fun greetDelayed(delayMillis: Long) {
    delay(delayMillis)
    println("Hello, World!")
}
```

- suspend 키워드로 정의한 일시 중단 함수 역시 다른 일시 중단 함수와 동일하게 호출하기 위해서는 코루틴 빌더로 감싸야 한다.
```kotlin
fun main() {
    runBlocking {
        greetDelayed(1000)
    }
}
```

#### 비동기 함수로 구현
- 여기서 비동기 함수란 `launch()`, `async()` 빌더로 감싼 함수를 의미한다.
- 비동기 함수임을 알 수 있도록 네이밍
- 해당 요청이 완료될 때까지 일시정지 해야 하므로 `await()` 사용
```kotlin
class ProfileServiceClient {
    fun asyncFetchByName(name: String) = GlobalScope.async {
        Profile(1, name , 28)
    }

    fun asyncFetchById(id: Long) = GlobalScope.async {
        Profile(id, "Susan" , 28)
    }
}
```

#### 일시 중단 함수로 구현
- `suspend` 키워드를 이용해 일시중단 함수임을 명시
- 반환형 Deferred 제거할 수 있다.
```kotlin
class SuspendProfileServiceClient {
    suspend fun fetchByName(name: String): SuspendProfile {
        return SuspendProfile(1, name , 28)
    }

    suspend fun fetchById(id: Long): SuspendProfile {
        return SuspendProfile(id, "Susan" , 28)
    }
}
```

#### 비동기 함수와 일시 중단 함수 차이
- 유연함 : 비동기 함수는 구현이 Deferred와 엮이게 됨으로 유연함이 떨어진다. 일시 중단 함수를 쓰면 Future를 지원하는 모든 라이브러리를 구현에서 사용 가능.
- 간단함 : 순차적으로 수행하려는 작업에 비동기 함수를 사용하면 항상 `await()`를 호출해야 하는 번거로움이 생기고, 명시적으로 async가 포함된 함수 네이밍을 정의해야 한다.

#### 비동기 함수 대신 일시 중단 함수 사용 가이드라인
- 구현에 Job이 엮이는 것을 피하기 위해 일시 중단 함수 사용
- 인터페이스 정의할 때 항상 일시 중단 함수 사용. 비동기 함수를 사용하면 Job을 반환하기 위한 구현을 해야 한다.
- 추상 메소드를 정의할 때는 항상 일시 중단 함수를 사용한다.

### 코루틴 컨텍스트
- **코루틴은 항상 컨텍스트 안에서 실행된다.**
- 컨텍스트는 코루틴이 어떻게 실행되고 동작해야 하는지를 정의할 수 있게 해주는 요소들의 그룹이다.

### 디스패처
- 대스패처는 코루틴이 실행될 스레드를 결정한다. 시작될 곳, 중단 후 재개될 곳을 모두 결정한다.

#### CommonPool
- CPU 바운드 작업을 위해서 프레임워크에 의해 자동으로 생성되는 스레드 풀이다.
- 스레드 풀의 최대 크기는 시스템의 코어 수에서 1을 뺀 값이다.
- 현재는 CommonPool을 직접 사용할 수 없다. `Dispathcers.Default` 가 기본 디스패처이다. (CPU 코어 개수만큼 생성되는 스레드 풀)
```kotlin
GlobalScope.launch(CommonPool) { // 지원 종료
    // TODO: Implement CPU-bound algorithm here
}
```

```kotlin
GlobalScope.launch(Dispatchers.Default) {
    // TODO: Implement CPU-bound algorithm here
}
```

#### Unconfined
- 첫 번째 중단 지점에 도달할 때까지 현재 스레드에 있는 코루틴을 실행한다.
- 중단 후 다음 코루틴이 실행되었던 스레드에서 재개된다.
```text
Starting in Thread[main,5,main]
Resuming in Thread[kotlinx.coroutines.DefaultExecutor,5,main]
```

#### 단일 스레드 컨텍스트
- 항상 코루틴이 특정 스레드 안에서 실행된다는 것을 보장한다.
- `newSingleThreadContext()`를 사용한다.
```text
Starting in Thread[newThread,5,main]
Resuming in Thread[newThread,5,main]
```

#### 스레드 풀
- 스레드 풀을 갖고 있으며 해당 풀에서 가용한 스레드에서 코루틴을 시작하고 재개한다.
- 런타임이 가용한 스레드를 정하고 부하 분산을 위한 방법도 정하기 때문에 따로 할 작업은 없다.
- `newFixedThreadPoolContext()`를 사용한다.
```text
Starting in Thread[myPool-1,5,main]
Resuming in Thread[myPool-2,5,main]
```

### 에외 처리
- 코루틴 컨텍스트의 또 다른 중요한 용도는 예측이 어려운 예외(uncaught exception)에 대한 동작을 정의하는 것이다.
- `CoroutineExceptionHandler를` 구현해 만들 수 있다.
```kotlin
val handler = CoroutineExceptionHandler { context, throwable ->
    println("Error captured in $context")
    println("Message : ${throwable.message}")
}

GlobalScope.launch(handler) {
    TODO("Not implemented yet")
}
```

```text
Error captured in [chapter04._08_예외처리Kt$main$1$invokeSuspend$$inlined$CoroutineExceptionHandler$1@5b2beb70, StandaloneCoroutine{Cancelling}@661a3b36, DefaultDispatcher]
Message : An operation is not implemented: Not implemented yet
```

#### Non-cancellable 
- 코루틴의 실행이 취소되면 코루틴 내부에 `CancellationException` 유형의 예외가 발생하고 코루틴이 종료된다.
- 코루틴 내부에서 예외가 발생하기 때문에 try-finally 블록을 사용해 예외를 처리할 수 있다.
```kotlin
val job = launch {
    try {
        while (isActive) {
            delay(500)

            println("still running")
        }
    } finally {
        println("cancelled, will delay finalization now")
        delay(5000) // 실제로 동작하지 않음. 취소 중인 코루틴은 일시 중단될 수 없도록 설계됐다.
        println("delay completed, bye")
    }
}

delay(1200)
job.cancelAndJoin()
```

- 취소 중인 코루틴은 일시 중단될 수 없도록 설계됐다.
- 코루틴이 취소되는 동안 일시 중지가 필요한 경우 NonCancellable 컨텍스트를 사용해야 한다.
```kotlin
finally {
    withContext(NonCancellable) {   // 코루틴의 취소 여부와 관계없이 withContext()에 전달된 일시 중단 람다가 일시 중단될 수 있도록 보장한다.
        println("cancelled, will delay finalization now")
        delay(5000)
        println("delay completed, bye")
    }
}
```

### 컨텍스트에 대한 추가 정보
- 컨텍스트는 코루틴이 어떻게 동작할지에 대한 다른 세부사항들을 많이 정의할 수 있다.
- 컨텍스트는 결합된 동작을 정의해 작동하기도 한다.

### 컨텍스트 결합 

#### 컨텍스트 조합
- 특정 스레드에서 수행하는 코루틴을 실행하고 동시에 해당 스레드를 위한 예외처리 설정 예제
- 더하기 연산자 이용해 요소 결합
```kotlin
val dispatcher = newSingleThreadContext("myDispatcher")
val handler = CoroutineExceptionHandler { _, throwable ->
    println("Error captured")
    println("Message: ${throwable.message}")
}

GlobalScope.launch(dispatcher + handler) {
    println("Running in ${java.lang.Thread.currentThread().name}")
    TODO("Not implemented")
}.join()
```

#### 컨텍스트 분리
- 결합된 컨텍스트에서 컨텍스트 요소를 제거할 수도 있다. 
- 요소를 제거하기 위해서는 제거할 요소의 키에 대한 참조가 있어야 한다.
```kotlin
val context = dispatcher + handler
val tmpContext = context.minusKey(dispatcher.key)   // 컨텍스트에서 디스패처 요소 제거
```

#### withContext를 사용하는 임시 컨텍스트 스위치
- `withContext()`는 코드 블록 실행을 위해 주어진 컨텍스트를 사용할 일시 중단 함수이다.
- withContext는 Job이나 Deferred를 반환하지 않는다. 전달한 람다의 마지막 구문에 해당하는 값을 반환할 것이다.
- `join()`이나 `await()`를 호출할 필요 없이 context가 종료될 때까지 일시 중단된다.
```kotlin
val dispatcher = newSingleThreadContext("myThread")
val name = withContext(dispatcher) {
    "Tester"
}

println("name : $name")
```










