## Chapter 05. 이터레이터, 시퀀스 그리고 프로듀서

5장에서 다루는 내용
- 일시 중단 가능한 시퀀스(Suspendable sequence)
- 일시 중단 가능한 이터레이터(Suspendable iterator)
- 일시 중단 가능한 데이터 소스에서 데이터 산출
- 시퀀스와 이터레이터의 차이점
- 프로듀서(Producer)를 사용한 비동기 데이터 검색
- 프로듀서의 실제 사례

### 이터레이터
- 이터레이터는 요소들의 컬렉션을 순서대로 살펴보는 데 특히 유용하다.
- 코틀린 이터레이터의 특징
   - 인덱스로 요소를 검색할 수 없으므로, 순차 액세스만 가능하다.
   - hasNext() 함수로 더 많은 요소가 있는지 알 수 있다.
   - 요소는 한 방향으로만 검색할 수 없다. 이전 요소 검색 불가.
   - 재설정 할 수 없으므로 한 번만 반복할 수 있다.
- `iterator()` 빌더 사용 (코틀린 1.3부터 iterator 사용)
- 기본적으로 Iterator<T>를 리턴한다.
```kotlin
val iterator = iterator {
    yield(1)  // Iterator<Int>
}
```

#### 모든 요소 살펴보기
- 전체 이터레이터를 반복하기 위해서는 `forEach()`나 `forEachRemaining()` 함수를 사용할 수 있다.
```kotlin
iterator.forEach {
    println(it)
}
```

#### 다음 값 가져오기
- `next()`를 이용해 이터레이터의 요소를 읽을 수 있다.
```kotlin
iterator.next()
```

#### 요소가 더 있는지 검증하기
- `hasNext()`를 이용해 이터레이터에 다음 요소가 있는지 확인 할 수 있다. 요소가 있으면 true, 없으면 false를 반환한다.
```kotlin
iterator.hasNext()
```

#### 요소를 검증하지 않고 next() 호출하기
- `next()`로 이터레이터에서 요소를 가져올 때는 항상 `hasNext()`를 호출하는 것이 안전하다.
- `next()`를 호출했지만 더 이상 가져올 요소가 없으면 `NoSuchElementException`이 발생한다.
```kotlin
val iterator = iterator {
    yield(1)
}
println(iterator.next())
println(iterator.next())  // NoSuchElementException 발생
```

#### hasNext()의 내부 작업에 대한 참고사항
- `hasNext()`가 작동하려면 런타임은 코루틴 실행을 재개한다.
- `hasNext()` 호출로 인해 값이 산출되면 값이 유지되다가 다음에 `next()`를 호출할 때 값이 반환된다.
```kotlin
  val iterator = iterator {
      println("yielding 1")
      yield(1)
      println("yielding 2")  // hasNext() 호출하면 이터레이터가 두 번째 값 생성
      yield(2)
  }

  iterator.next()

  if (iterator.hasNext()) {
      println("iterator has next")
      iterator.next()
  }
```

### 시퀀스
- 인덱스로 값을 가져올 수 있다.
- 상태가 저장되지 않으며(stateless) 상호작용한 후 자동으로 재설정(reset) 된다.
- 한 번의 호출로 값 그룹을 가져올 수 있다.
- 일시 중단 시퀀스를 만들기 위해 `sequence()` 빌더를 사용한다. (코틀린 1.3부터 sequence 사용)
```kotlin
val sequence = sequence {
    yield(1)
}
```

#### 시퀀스의 모든 요소 읽기
- 시퀀스의 모든 요소를 살펴보기 위해 `forEach()`, `forEachIndexed()`를 사용할 수 있다.
- `forEachIndexed()`는 값과 함께 값의 인덱스를 제공하는 확장 함수다.
```kotlin
sequence.forEach {
    println("$it ")
}

sequence.forEachIndexed { index, value ->
  println("$index is $value ")
}
```

#### 특정 요소 얻기
- `elementAt()`을 이용해 인덱스를 가져와 해당 위치의 요소를 반환한다.
```kotlin
sequence.elementAt(4)
```
- `elementAtOrElse()` 함수는 주어진 인덱스에 요소가 없으면 람다로 실행된다. 람다는 전달된 인덱스를 받는다.
```kotlin
sequence.elementAtOrElse(5) { it * 10 }
```
- `elementAtOrNull` 인덱스를 가져와서 T?를 반환한다. 인덱스에 요소가 없으면 null 반환
```kotlin
sequence.elementAtOrNull(5)
```
- `take()` 요소 그룹 얻기
- take()는 중간 연산이므로 종단연산이 호출되는 시점에 계산돼 Sequence<T>를 반환한다.
- take 한 요수 개수보다 실제 시퀀스의 요소 개수가 적으면, take 한 요소 개수만큼만 반환한다.
```kotlin
val firstFive = sequence.take(5)
println(firstFive.joinToString())
```

#### 시퀀스는 상태가 없다.
- 일시 중단 시퀀스는 상태가 없고(stateless), 사용된 후에 재설정(reset) 된다.
- 이터레이터를 사용하는 것과 달리 시퀀스는 각각의 호출마다 요소의 처움부터 실행된다. (index 0부터 실행)

#### 피보나치
```kotlin
  val fibonacci = sequence {
      yield(1)
      var current = 1
      var next = 1

      while(true) {
          yield(next)
          val tmpNext = current + next
          current = next
          next = tmpNext
      }
  }
```

### 프로듀서
- 시퀀스와 이터레이터는 실행 중에 일시 중단할 수 없다는 제한이 있다.
- 프로듀서의 중요한 세부 사항
   - 프로듀서는 값이 생성된 후 일시 중단되며, 새로운 값이 요청될 때 재개된다.
   - 프로듀서는 특정 CoroutineContext로 생성할 수 있다.
   - 전달되는 일시 중단 람다의 본문은 언제든지 일시 중단될 수 있다.
   - 어느 시점에서든 일시 중단할 수 있으므로 프로듀서의 값은 일시 중단 연산에서만 수신할 수 있다.
   - 채널을 사용해 작동하므로 데이터를 스트림처럼 생각할 수 있다. 요소를 수신하면 스트림에서 요소가 제거된다.
- 프로듀서를 만들려면 코루틴 빌더 `produce()`를 호출해야 한다. ReceiveChannel<E>을 리턴한다.
- 프로듀서의 요소를 산출하기 위해 `send(E)` 함수를 사용한다.

```kotlin
val producer = GlobalScope.produce {
    send(1)
}
```

- `launch()`, `async()`와 같은 방식으로 CoroutineContext를 지정할 수 있다.
```kotlin
val context = newSingleThreadContext("myThread")

val producer = GlobalScope.produce(context) {
    send(1)
}
```

#### 프로듀서와 상호작용
- 프로듀서와의 상호작용은 시퀀스와 이터레이터를 사용해 수행되는 방식을 혼합한 것이다.
- 다음장에서 Channel에 대해 자세히 알아보고 이번장에서는 `ReceiveChannel`의 일부 기능에 대해서만 설명.

#### 프로듀서의 모든 요소 읽기
- `consumeEach()` 함수를 이용해 프로듀서의 모든 요소 읽기
```kotlin
val context = newSingleThreadContext("myThread")

val producer = GlobalScope.produce(context) {
    for (i in 0..9) 
        send(i)
}
```

#### 단일 요소 받기
- `receive()` 함수를 이용해 단일 요소 읽기
```kotlin
producer.receive()
```

#### 요소 그룹 가져오기
- `take()` 함수를 이용해 입력한 개수만큼 묶음으로 요소를 가져올 수 있다. (코틀린 1.4에서 deprecated)
- 시퀀스와 동일하게 `take()`는 중간 연산이므로 종단 연산이 발생할 때 요소의 실제 값이 계산된다.
```kotlin
producer.take(3).consumeEach {
    println(it)
}
```

#### 사용 가능한 요소보다 더 많은 요소 사용하기
- 프로듀서는 더 이상 요소가 없으면 중지된다.
- 프로듀서가 실행을 완료하면 채널이 닫히기 때문에 중단이 발생한다. `ClosedReceiveChannelException` 예외가 발생한다.
- isClosedForReceive() 로 검증
