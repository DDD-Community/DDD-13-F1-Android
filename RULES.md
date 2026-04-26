# Quiket Rules

## Module direction
- `:app`은 조립만 담당한다.
- 의존 방향은 `app -> feature -> core`로 유지한다.
- `core` 모듈은 서로 최소한으로만 연결한다.
- 실제 기능은 `:feature:*`에 추가한다.
- 실제 앱 기본 흐름은 `splash -> onboarding/login -> main`으로 유지한다.
- 바텀탭의 실제 기능은 `home`, `history`, `review`, `mypage` 각 feature가 맡는다.
- `:feature:main`은 바텀탭 shell 역할만 담당한다.
- `:feature:sample`은 PokéAPI reference sample이며, 실제 서비스 엔트리와 분리해 둔다.

## Feature structure
- 기본 구조는 `presentation`, `data`, `domain` 패키지다.
- `domain`은 optional이다.
- 복잡한 비즈니스 로직, 재사용 로직, 테스트 분리 가치가 있을 때만 `usecase`를 만든다.
- 단순 화면 로딩은 `ViewModel + repository`만으로 허용한다.
- reference sample 전용 UI, 네트워크, 비즈니스 로직은 `core`로 올리지 않는다.
- shared Room DB를 쓸 경우 schema/dao/provider는 `core:database`에 둔다.
- `core:database` 내부 패키지는 `db`, `di`, `dao`, `entity` 기준으로 유지한다.

## MVI/UDF rule
- 화면 상태는 `State` 하나로 관리한다.
- 사용자 입력과 화면 액션은 `Intent`로 받는다.
- 네비게이션, 스낵바 같은 일회성 이벤트는 `Effect`로 분리한다.
- 모든 화면 ViewModel은 `MviViewModel`을 기반으로 한다.
- UI Composable에는 비즈니스 로직을 두지 않는다.
- Compose 화면의 Route/Screen 분리와 state ownership은 `docs/compose-screen-conventions.md`를 따른다.

## Core promotion rule
- 공통 코드는 두 feature 이상에서 실제 중복이 생겼을 때만 `core`로 승격한다.
- 특정 feature에서만 쓰는 mapper, model, helper는 해당 feature 안에 둔다.
- `core`를 새로운 모놀리스로 키우지 않는다.

## Asset and resource naming
- 공통 브랜딩 리소스는 `:core:designsystem`에 둔다.
- feature 전용 리소스는 해당 feature module에 둔다.
- raw asset 디렉터리는 `app/src/main/assets/lottie`, `json`, `html`을 사용한다.
- 네이밍 규칙은 `ic_*`, `img_*`, `illust_*`, `bg_*`, `anim_*`를 따른다.

## Testing rule
- ViewModel과 repository는 기본적으로 테스트 대상이다.
- UI 테스트는 smoke 수준이라도 각 핵심 feature에 1개 이상 둔다.
- fake 또는 in-memory 구현으로 데이터 흐름을 검증할 수 있게 설계한다.
