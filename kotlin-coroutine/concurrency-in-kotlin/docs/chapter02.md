## Chapter 02. 코루틴 인 액션

### 안드로이드의 UI 스레드
- 안드로이드 애플리케이션에는 UI를 업데이트 하고 사용자와의 상호작용을 리스닝하며, 메뉴를 클릭하는 것과 같은 사용자에 의해 생성된 이벤트 처리를 전담하는 스레드가 있다. 

### CallFromWrongThreadException
- 안드로이드는 뷰 계층을 생성하지 않은 스레드가 관련 뷰를 업데이트하려고 할 때마다 CalledFromWrongThreadException을 발생시킨다. 
- UI 스레드만이 뷰 계층을 생성할 수 있는 스레드이며 뷰를 항상 업데이트 할 수 있다.

### NetworkOnMainThreadException
- 자바에서의 네트워크 동작은 기본적으로 블로킹된다.
- 백그라운드에서 요청하고, UI 스레드에서 업데이트 할 것
- 백그라운드 스레드가 웹 서비스를 호출하고, 응답이 처리된 후에 UI 스레드에서 UI를 업데이트하도록 해야 한다.

### CoroutineDispatcher
- 코틀린에서는 스레드와 스레드 풀을 쉽게 만들 수 있지만 직접 액세스하거나 제어하지 않는다는 점을 알아야 한다. 
- 여기서는 CoroutineDispatcher를 만들어야 하는데, 이것은 기본적으로 가용성, 부하, 설정을 기반으로 `스레드 간에 코루틴을 분산하는 오케스트레이터`다.
```kotlin
val newDispatcher = newSingleThreadContext(name = "ServiceCall")
```

### async 코루틴 시작
- 결과 처리를 위한 목적으로 코루틴을 시작했다면 async()를 사용해야 한다. 
- async()는 Deferred<T>를 반환한다. async를 사용할 때 결과를 처리하는 것을 잊어서는 안 된다.
```kotlin
fun main(args: Array<String>) = runBlocking {
	val task = GlobalScope.async {
		doSomething()
	}
	task.join()
	println("Completed")
}

fun doSomething() {
	throw UnSupportedOperationException("Can't do")
}
```

- async() 블록 안에서 발생하는 예외는 그 결과에 첨부되는데, 그 결과를 확인해야 예외를 찾을 수 있다. 
- isCancelled와 getCancellationException() 메소드를 함께 사용해 안전하게 예외를 가져올 수 있다.
```kotlin
if (task.isCancelled){
	val exception = task.getCancellationException()
	println("Error with message : ${exception.cause}")
} else {
	println("Success")
}
```

- await()를 호출해서 중단되는데 이 경우가 예외를 감싸지 않고 전파하는, 감싸지 않은 디퍼드다 (unwrapping deferred) 
- `join()은 예외를 전파하지 않고 처리하는 반면, await()는 단지 호출하는 것만으로는 예외가 전파된다.`

### launch 코루틴 시작
- 결과를 반환하지 않는 코루틴을 시작하려면 launch()를 사용해야 한다. 
- launch()는 연산이 실패한 경우에만 통보 받기를 원하는 파이어-앤-포겟(fire-and-forget) 시나리오를 위해 설계됐으며, 필요할 때 취소할 수 있는 함수도 함께 제공된다.
- `fire-and-forget` : 실행 후 결과에 대해서 신경 쓸 필요가 없는 경우. 이벤트나 메시지 기반 시스템에서 널리 사용되는 패턴.
```kotlin
fun main(args: Array<String>) = runBlocking {
	val task = GlobalScope.launch {
		doSomething()
	}
	task.join()
	println("Completed")
}

fun doSomething() {
	throw UnSupportedOperationException("Can't do")
}
```
- 예외가 스택에 출력되었지만 실행이 중단되지 않았고, 애플리케이션은 main()의 실행을 완료했다는 것을 알 수 있다.
```text
Exception in thread "DefaultDispatcher-worker-1" java.lang.UnsupportedOperationException
	at chapter02.디스패처_launchKt.doSomething2(디스패처 launch.kt:17)
	at chapter02.디스패처_launchKt$main$1$task$1.invokeSuspend(디스패처 launch.kt:9)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:56)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:571)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:738)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:678)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:665)
Completed

Process finished with exit code 0   // 정상 종료
```

- 코드를 실행하면 기본적으로 코루틴이 DefaultDispatcher에서 실행된다. 지정된 스레드에서 코루틴을 실행시킬 수 있다.
```kotlin
fun main() {
	val dispathcer = newSingleThreadContexet(name = "ServiceCall")
	val task = GlobalScope.launch(dispatcher) {
		printCurrentThread()
	}
	task.join()
}
```
- 스레드의 이름이 디스패처에 대해 설정된 이름과 같다.
```text
Running in thread [ServiceCall]
Running in thread [main]

Process finished with exit code 0
```

### 유연한 디스패처를 가지는 비동기 함수
- 디스패처를 함수의 선택적 파라미터로 설정해서 함수에 유연성을 줄 수 있다.
```kotlin
private val defDsp = newSingleThreadContext(name = "ServiceCall")
private fun asyncLoadNews(dispatcher: CoroutineDispatcher = defDsp) = 
	GlobalScope.launch(dispatcher) {
	  ...
	}
```

### 더 좋은 방식을 선택하기 위한 방법
- 여러번 호출돼야 한다면 launch()나 async() 블록으로 동기 함수를 감싸는 것이 좋다.
- 같은 코드 조각을 모든 클래스에 전체적으로 적용해야 할 때는 비동기 함수에 만드는 편이 가독성을 높일 수 있다.
- 함수 네이밍을 명확하게 정의해야 한다. launch, async를 품고 있는 함수인지 호출부에서 명확하게 알고 있어야 한다. 그렇지 않으면 레이스 컨디션이나 기타 동시성 문제가 발생할 수 있다.

### 요약
- 안드로이드에서는 네트워크 요청이 UI 스레드 상에서 수행된다면 NetworkOnMainThreadException을 발생시킨다. UI 업데이트는 UI 스레드에서만 수행할 수 있다. 네트워크 요청은 백그라운드에서 수행되어야 한다.
- CoroutineDispatcher는 코루틴을 특정 스레드 또는 스레드 그룹에서 실행하도록 할 수 있다.
