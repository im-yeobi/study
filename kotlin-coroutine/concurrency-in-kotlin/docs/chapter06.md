## Chapter 06. 채널 - 통신을 통한 메모리 공유

- 동시성과 관련된 수많은 오류는 서로 다른 스레드 간에 메모리를 공유할 때 발생한다.
- 데드락, 레이스 컨디션, 원자성 위반 은 공유 상태와 관련이 있다. (상태가 일관성을 잃어버리는 원인이 되기도 한다.)
- 이런 문제를 극복하기 위해 코틀린, 고, 다트와 같은 최신 프로그래밍 언어들은 `채널`을 제공하고 있다.

### 채널의 이해
- 채널은 동시성 코드 간에 서로 안전한 통신을 할 수 있도록 해주는 도구이다.
- 채널은 스레드가 `서로 상태를 공유하는 대신 메시지를 주고받는 통신`을 하도록 함으로써 동시성 코드를 작성하는 데 도움을 준다.
- 채널은 서로 다른 코루틴 간에 메시지를 안전하게 보내고 받기 위한 파이프라인이다.
- 코루틴이 채널로부터 가져온 데이터를 처리하는 작업이 가능해지면서 부하 분산에 대해 걱정할 필요가 없어진다.

### 채널 유형과 배압
- `Channel`의 send()는 일시 중단 함수다. 
- 채널을 통해 데이터를 보내는 코루틴은 채널 안의 요소가 버퍼 크기에 도달하면 일시 중단된다.
- 채널에서 요소가 제거되는 즉시 송신자는 다시 재개된다.

#### 언버퍼드 채널
- 버퍼가 없는 채널을 언버퍼드 채널이라 한다.

**1) RendezvousChannel**
- 언버퍼드 채널의 구현. 채널 구현은 버퍼가 전혀 없어서 그 채널에서 send()를 호출하면 리시버가 receive()를 호출할 때까지 일시 중지된다.
```kotlin
val channel = Channel<Int>()
```

```kotlin
// Use Channel() factory function to conveniently create an instance of rendezvous channel.
val channel = RendezvousChannel<Int>()
```

#### 버퍼드 채널
- 버퍼를 가지는 채널이다.
- `채널 내 요소의 수가 버퍼의 크기와 같을 때마다 송신자의 실행을 중지한다.`
- 버퍼의 크기에 따라 몇 가지 종류의 버퍼드 채널이 있다.

**1) LinkedListChannel**
- 중단 없이 무한의 요소를 전송할 수 있는 채널
- 이 채널의 유형은 어떤 송신자도 중단하지 않는다. (일시 중지하지 않는다.)
- `LinkedListChannel`은 메모리를 너무 많이 소모할 수 있기 때문에 사용할 때 주의해야 한다.
- 요구사항과 대상 디바이스에 기반하는 버퍼 크기를 갖는 버퍼드 채널을 사용하는 것을 권장한다.
```kotlin
val channel = Channel<Int>(Channel.UNLIMITED)
```

```kotlin
// This an internal API and should not be used from general code.
val channel = LinkedListChannel<Int>(Channel.UNLIMITED)
```

**2) ArrayChannel**
- 버퍼 크기를 0부터 최대 int.MAX_VALUE - 1까지 가지며, 가지고 있는 요소의 양이 버퍼 크기에 이르면 송신자를 일시 중단한다.
```kotlin
val channel = Channel<Int>(10)  // 버퍼 10
```

```kotlin
// This channel is created by Channel(capacity) factory function invocation.
val channel = ArrayChannel<Int>(10)
```

**3) ConflatedChannel**
- 내보낸 요소가 유실돼도 괜찮다는 생각이 깔려 있다.
- 하나의 요소의 버퍼만 갖고 있고, 새로운 요소가 보내질 때마다 이전 요소는 유실된다.
- 송신자가 절대 일시중지 되지 않는다.
```kotlin
val channel = Channel<Int>(Channel.CONFLATED)  // 버퍼 10
```

```kotlin
// This channel is created by Channel(Channel.CONFLATED) factory function invocation.
val channel = ConflatedChannel<Int>()
```

### SendChannel
- 채널을 통해 요소를 보내기 위한 함수와 보낼 수 있는지 검증하기 위한 함수를 정의한다.

#### 보내기 전 검증
- 전송을 위해 채널이 닫히지 않았는지 확인한다.
- isClosedForSend
   ```kotlin
   val channel = Channel()
   channel.isClosedForSend()   // false
   channel.close()
   channel.isClosedForSend()   // true
   ```        

#### 요소 전송
- 채널을 통해 요소를 전송하려면 `send()` 함수를 사용해야 한다.
- 함수는 버퍼드 채널에서 버퍼가 가득 차면 송신지를 일시 중단한다.
- 채널이 닫히면 send() 함수는 `ClosedChannelException` 발생
