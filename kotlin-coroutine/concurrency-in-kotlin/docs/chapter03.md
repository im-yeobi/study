## Chapter 03. 라이프 사이클과 에러 핸들링
- 3장에서는 두 가지 유형의 비동기 작업, 잡(Job) 및 디퍼드(Deferred)에 대해 자세히 살펴본다.
- 두 유형의 유사점과 차이점은 무엇인지 그리고 이들의 라이프 사이클에 대해서도 자세히 알아본다.

### 잡과 디퍼드
- 결과가 없는 비동기 함수 : 일반적인 시나리오로는 로그에 기록하고 분석 데이터를 전송하는 것과 같은 백그라운드 작업을 들 수 있다.
- 결과를 반환하는 비동기 함수 : 비동기 함수가 웹 서비스에서 정보를 가져올 때 거의 대부분 해당 함수를 사용해 정보를 반환하고자 할 것이다. 

### 잡 (Job)
- 잡은 파이어-앤-포겟(fire and forget) 작업이다.
- 한 번 시작된 작업은 예외가 발생하지 않는 한 대기하지 않는다.
- 잡은 인터페이스로 launch()와 Job()은 모두 JobSupport의 구현체를 반환한다.
- JobSupport : 잡을 확장한 인터페이스인 Job.Deferred의 여러 구현체의 기반이다. 현재 코루틴 API 내부에서만 사용되고 있으며, 추후 삭제될 수 있다.
```kotlin
runBlocking {
    val job = GlobalScope.launch {
        // 백그라운드 작업 수행
        // fire and forget
    }
}
```

#### 예외 처리
- 기본적으로 잡 내부에서 발생하는 예외는 잡을 생성한 곳까지 전파된다.
- 코루틴이 수행되고 있는 스레드에 포착되지 않은 예외 처리기(Uncaught Exception Handler)에 예외가 전파된다.
```text
Exception in thread "DefaultDispatcher-worker-1" kotlin.NotImplementedError: An operation is not implemented: Not Implemented
	at chapter03.잡_예외처리Kt$main$1$1.invokeSuspend(잡_예외처리.kt:11)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:56)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:571)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:738)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:678)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:665)
test

Process finished with exit code 0
```

#### 라이프 사이클
- 기본적으로 잡은 생성되는 즉시 시작된다. 잡을 생성할 때 시작하지 않게 하는 것도 가능하다.
- 잡이 특정 상태에 도달하면 이전 상태로 되돌아가지 않는다.

#### 1) 생성
- 잡은 기본적으로 launch()나 Job()을 사용해 생성될 때 자동으로 시작된다.
- 잡을 생성할 때 자동으로 시작되지 않게 하려면 `CoroutineStart.LAZY`를 사용해야 한다.
```kotlin
runBlocking {
    GlobalScope.launch(start = CoroutineStart.LAZY) {
        // LAZY : 자동 시작되지 않음
        TODO("Not Implemented")
    }
    delay(1000)
}
```

#### 2) 활성
- start() : 잡이 완료될 때까지 기다리지 않고 잡을 시작. 실행을 일시중단 하지 않으므로 코루틴 또는 일시중단 함수에서 호출하지 않아도 된다.
- join() : 잡이 완료될 때까지 실행을 일시 중단 `Suspends the coroutine until this job is complete.` 코루틴 또는 일시중단 함수에서 사용해야 한다.
- 시작된 모든 잡은 활성 생태이며 실행이 완료되거나 취소가 요청될 때까지 활성 상태가 된다.
```kotlin
runBlocking {
    val job = GlobalScope.launch {
        TODO("Not Implemented")
    }

    // 잡 완료될 때까지 기다리지 않는다.
//        job.start()

    // 잡 완료될 때까지 기다린다.
    job.join()
}
```

#### 3) 취소 중
- 취소 요청을 받은 활성 잡은 취소 중(canceling)이라고 하는 스테이징 상태로 돌아갈 수 있다. 
- cancel() 함수 호출
- 잡 실행은 2초 후에 취소된다.
- cancelAndJoin() : 실행을 취소할 뿐 아니라 취소가 완료될 때까지 현재 코루틴을 일시 중단한다.
```kotlin
runBlocking {
    val job = GlobalScope.launch {
        delay(1000)
    }

    delay(2000)
    job.cancel()    // 잡 취소
    job.cancelAndJoin() // 잡 취소 & 취소가 완료될 때까지 현재 코루틴 일시 중단
}
```

#### 4) 취소됨
- 취소 또는 취소되지 않은 예외로 인해 실행이 종료된 잡은 취소됨(cancelled)으로 간주된다.
- 잡이 취소되면 getCancellationException() 함수를 통해 취소에 대한 정보를 얻을 수 있다. => 최신 버전에서는 코루틴 내부 API에서만 사용됨
- 취소된 잡과 예외로 인해 실패한 잡을 구별하기 위해 `CoroutineExceptionHandler`를 설정해 취소 작업을 처리할 수 있다.- 
```kotlin
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
```
- invokeOnCompletion()을 사용할 수 있다.
```kotlin
runBlocking<Unit> {
    GlobalScope.launch {
        TODO("Not Implemented")
    }.invokeOnCompletion { cause ->
        cause?.let {
            println("Job cancelled due to ${cause.message}")
        }
    }
}
```

#### 5) 완료됨
- 실행이 중지된 잡은 완료됨(completed)으로 간주된다. 
- 실행이 정상적으로 종료됐거나 취소됐는지 또는 예외 때문에 종료됐는지 여부에 관계없이 적용된다.

#### 6) 잡의 현재 상태 확인
- 잡에는 상태가 많아서 외부에서 현재 상태를 파악하는 방법을 알아야 한다.
- `isActive` : 잡이 활성 상태인지 여부, 잡이 일시 중지인 경우도 true를 반환한다.
- `isCompleted` : 잡이 실행을 완료했는지 여부
- `isCancelled` : 잡 취소 여부. 취소가 요청되면 즉시 true가 된다.

### 디퍼드 (Deferred)
- 디퍼드(Deferred, 지연)는 결과를 갖는 비동기 작업을 수행하기 위해 잡을 확장한다.
- 다른 언어에서 퓨처(Futures) 또는 프로미스(Promises)라고 하는 것의 코틀린 구현체가 디퍼드다.
- 디퍼드와 그 상태의 라이프 사이클은 잡과 비슷하다. 
- 차이점은 반환 유형과 에러 핸들링이다. 

### 예외 처리
- 잡과 달리 디퍼드는 처리되지 않은 예외를 자동으로 전파하지 않는다. 실행이 성공했는지 확인하는 것은 사용자의 몫이다.
```kotlin
runBlocking {
    val deferred = GlobalScope.async {
        TODO("Not Implemented yet!")
    }

    // wait for it to fail
    //        delay(2000)
    deferred.await()
}
```
- try..catch 를 이용한 코루틴 예외 핸들링
```kotlin
try {
    deferred.await()
} catch (throwable: Throwable) {
    println("catch exception ${throwable.message}")
}
```

### 요약
- 잡은 아무것도 반환하지 않는 백그라운드 작업에 사용된다.
- 디퍼드는 백그라운드 작업이 수신하려는 것을 반환할 때 사용된다.
- join()을 사용해 디퍼드가 대기된 경우, 예외가 전파되지 않도록 값을 읽기 전에 취소됐는지 여부를 확인해야 한다.
- 항상 잡에 예외를 기록하거나 표시하자.
