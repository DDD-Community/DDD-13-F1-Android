# QuiketApp

Quiket은 학습 기록, 문제 풀이, 오답 관리 기능을 중심으로 개발 중인 안드로이드 앱이다.

현재 저장소는 `실제 앱 뼈대`와 `기술 레퍼런스 샘플`이 함께 들어 있는 상태다.

## 현재 기준 상태

### 실제 앱 뼈대
- 루트 앱 흐름은 `Splash -> Login -> Main`까지 연결되어 있다.
- `Main`은 바텀탭 shell 역할만 하고, 실제 탭 기능은 각 feature 모듈이 담당한다.
- 바텀탭은 다음 4개로 나뉜다.
  - `홈` -> `:feature:home`
  - `기록` -> `:feature:history`
  - `오답노트` -> `:feature:review`
  - `마이` -> `:feature:mypage`
- 현재 각 탭 feature는 화면 뼈대와 navigation 진입점까지 만들어져 있고, 실제 비즈니스 로직은 아직 붙지 않았다.
- `Splash`는 아직 실제 로그인 상태를 읽지 않고, 임시로 로그인 화면으로 보내는 상태다.

### 레퍼런스 샘플
- `:feature:sample`은 PokéAPI 기반 reference sample이다.
- 이 샘플은 아래 기술을 묶어서 보여주는 예제 역할을 한다.
  - Hilt
  - Retrofit
  - Room
  - Paging3
  - Compose
  - lightweight MVI(UDF)
- 실제 앱 엔트리에서는 분리되어 있고, 필요할 때만 참고하는 용도로 남겨둔 상태다.

## 아키텍처 방향
- `feature-first`
- `lightweight MVI(UDF)`
- `domain optional`
- `regular multi-module`
- `Room`은 shared local DB로 보고 `:core:database`에서 관리

## 현재 모듈 구조

```text
Quiket/
├── build-logic/
├── app/
├── core/
│   ├── common/
│   ├── database/
│   ├── designsystem/
│   ├── navigation/
│   ├── network/
│   └── testing/
└── feature/
    ├── splash/
    ├── login/
    ├── main/
    ├── home/
    ├── history/
    ├── review/
    ├── mypage/
    └── sample/
```

## 모듈 역할

### `app`
- `Application`
- `MainActivity`
- 루트 `NavHost`
- feature graph 조립

### `core`
- `:core:common`
  - MVI base
  - dispatcher
  - 공통 런타임 유틸
- `:core:database`
  - shared Room DB
  - `db / di / dao / entity` 구조
- `:core:designsystem`
  - theme
  - 공통 UI 컴포넌트
  - 공통 리소스
- `:core:navigation`
  - route 규약
- `:core:network`
  - Json
  - OkHttp
  - Retrofit
- `:core:testing`
  - 공통 테스트 rule/helper

### `feature`
- `:feature:splash`
  - 앱 진입 판단 화면
- `:feature:login`
  - 로그인 진입 화면
- `:feature:main`
  - 바텀탭 shell
  - 내부 탭 navigation 조립
- `:feature:home`
  - 홈 탭
- `:feature:history`
  - 기록 탭
- `:feature:review`
  - 오답노트 탭
- `:feature:mypage`
  - 마이 탭
- `:feature:sample`
  - PokéAPI 기반 reference sample

## UI / Navigation 규약

현재 feature UI 구조는 아래 패턴을 기준으로 맞추고 있다.

- `Destination`
  - route 문자열 정의
- `Navigation`
  - `NavGraphBuilder` 확장 함수로 graph 등록
- `Route`
  - ViewModel 주입, state/effect wiring, navigation callback 연결
- `Screen`
  - 순수 UI 렌더링

즉 상위 모듈은 feature 내부 구현을 직접 알지 않고, 각 feature가 공개하는 route와 graph만 사용한다.

## 문서 정리

### [ARCHITECTURE.md](./ARCHITECTURE.md)
- 현재 채택한 아키텍처 방향
- 모듈 맵
- 실제 앱 흐름
- reference sample의 위치와 역할
- 새 feature를 추가할 때의 기준

### [RULES.md](./RULES.md)
- 모듈 의존 방향
- feature 내부 구조 규칙
- MVI/UDF 규칙
- core 승격 기준
- asset/resource 네이밍 규칙
- 테스트 기준

## 현재 구현 범위 요약


- 멀티모듈 구조
- build-logic convention plugin
- shared core 모듈 구성
- 실제 앱 기본 흐름 뼈대
- 바텀탭 4개 feature 분리
- sample reference feature 구축
- Room / Retrofit / Hilt / Paging3 예제 확보
