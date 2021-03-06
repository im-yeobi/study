## Chapter 08. 동시성 코드 테스트와 디버깅
- 동시성 코드 테스트를 위한 두 가지 원칙
   - `가정을 버려라`
   - `나무가 아닌 숲에 집중하라`
- 기능 테스트는 기능을 전체적으로 실행해 애플리케이션이 비동기적으로 작업을 수행하는 데 따르는 복잡성을 잘 표현하는 테스트를 작성할 수 있다.
- 애플리케이션의 안정성을 보장하기 위한 유일한 방법은 정확한 테스트를 하는 것이다.

### 디버깅
- 코루틴이 일정 개수 이상이면 전달받은 파라미터를 사용해서 각 코루틴을 추적하는 것은 번거로울 것이다.
- 코루틴이 고유한 식별자로 사용할 수 있는 것이 없다면 특정 요소와 관련된 엔트리를 정확하게 식별할 수 없다.

#### 자동 이름 지정
- `-Dkotlinx.coroutines.debug` VM 플래그를 전달해 코틀린이 현재 스레드의 이름을 요청할 때 생성되는 모든 코루틴에 고유 식별자를 부여한다.

#### 특정 이름 지정
-  

####
