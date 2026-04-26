# Quiket Architecture

## Chosen direction
- 기준은 `feature-first + lightweight MVI(UDF) + optional domain + regular multi-module`이다.
- Android 공식 권장처럼 `domain`은 선택 레이어로 취급한다.
- 멀티모듈은 유지하지만, 시작부터 feature를 지나치게 잘게 쪼개지 않는다.

## Module map
- `:app`: Hilt application, root activity, root nav host
- `:core:common`: MVI base, dispatcher, result abstraction
- `:core:database`: shared Room database, DAO/provider, entity package
- `:core:designsystem`: Compose theme, 공통 UI 컴포넌트, 공통 리소스
- `:core:navigation`: route 규약
- `:core:network`: Json, OkHttp, Retrofit
- `:core:testing`: 공통 테스트 rule과 helper
- `:feature:splash`: 앱 진입 판단 화면
- `:feature:onboarding`: 첫 진입 사용자 온보딩 화면
- `:feature:login`: 로그인 진입 화면
- `:feature:main`: 바텀탭 shell과 내부 탭 네비게이션
- `:feature:home`: 홈 탭 feature
- `:feature:history`: 기록 탭 feature
- `:feature:review`: 오답노트 탭 feature
- `:feature:mypage`: 마이 탭 feature
- `:feature:sample`: PokéAPI 기반 reference sample vertical slice

## App flow
1. 앱 시작점은 `:feature:splash`다.
2. `Splash`는 온보딩 완료 여부와 로그인 여부 판단 뒤 `Onboarding`, `Login`, 또는 `Main`으로 이동한다.
3. `Onboarding`은 첫 진입 사용자에게만 노출되는 소개 흐름이다.
4. `Main`은 비즈니스 feature가 아니라 바텀탭 shell이다.
5. 실제 탭 기능은 `Home`, `History`, `Review`, `MyPage` 각 feature가 맡는다.
6. shared DB를 쓸 경우 `:core:database`는 `db`, `di`, `dao`, `entity` 기준으로 단순하게 유지한다.

## Reference sample
1. `:feature:sample`은 Hilt, Retrofit, Room, Paging3 예제를 보여주는 reference sample이다.
2. 실제 서비스 네비게이션 엔트리에서는 분리해 두고, 필요할 때만 참고한다.
3. sample 전용 UI, API, mapper, repository는 실제 feature 기준이 아니라 예제 기준 코드다.

## Adding a new feature
1. 새 기능은 `:feature:*` 모듈로 추가한다.
2. 바텀탭에 속하면 `:feature:main`의 shell navigation에만 연결한다.
3. 로컬 저장이 필요하면 repository 구현에서 `:core:database`를 사용한다.
4. 공통으로 승격할 코드가 생기면 그때만 `core`로 이동한다.

## Build logic
- `build-logic` included build에서 공통 convention plugin을 제공한다.
- Android application, library, Compose, Hilt 설정은 module build script에서 직접 반복하지 않는다.
