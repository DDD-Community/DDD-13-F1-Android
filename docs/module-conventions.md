# Module Conventions

Use this before adding modules, navigation entries, Gradle wiring, or resources.

## Module Direction
- Keep dependency direction as `app -> feature -> core`.
- `:app` composes root app flow and should not own feature internals.
- `:feature:main` is the bottom-tab shell only.
- Put real product behavior in the owning `:feature:*` module.
- Promote code to `core` only after at least two real features need it.

## Feature Shape
- Use `presentation` for Route, Screen, ViewModel, and contract types.
- Use `navigation` for destination definitions and graph extension functions.
- Add `data` and `domain` only when the feature needs them.
- Keep `domain` optional; add use cases only for meaningful business logic or test separation.

## Navigation
- Each feature exposes a `Destination` that implements `QuiketDestination`.
- Feature graph builders should hide internal screens when the feature has more than one route.
- Root flow belongs in `:app`.
- Current root flow is `Splash -> Onboarding/Login -> Main`, with onboarding gated by completion state outside temporary QA overrides.

## Resources And Assets
- Common brand images, icons, typography, and shared UI assets belong in `:core:designsystem`.
- Feature-specific drawables belong in that feature module.
- Raw runtime assets use `app/src/main/assets`, grouped by type such as `lottie`, `json`, or `html`.
- Resource names follow `ic_*`, `img_*`, `illust_*`, `bg_*`, and `anim_*`.
- Avoid duplicating the same image across feature modules; move shared images to `:core:designsystem`.

## Gradle Wiring
- Add new modules to `settings.gradle.kts`.
- Use existing convention plugins instead of repeating Android/Compose setup manually.
- Add only the dependencies required by the module.
- Verify the feature module and app module after wiring.
