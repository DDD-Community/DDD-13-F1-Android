# Compose Screen Conventions

Use this before creating or changing Compose screens.

## Route And Screen
- Split feature entry points into `Route` and `Screen` by default.
- `Route` owns ViewModel injection, state collection, side effects, and navigation callbacks.
- `Screen` owns rendering and receives state plus event callbacks as parameters.
- Do not call `NavController` directly from `Screen`.

## State Ownership
- Prefer stateless `Screen` APIs for business state and durable UI state.
- Keep transient UI-only state inside `Screen` when it has no business meaning.
- Examples allowed inside `Screen`: `pagerState`, expanded menus, selected local tab, animation state, focus state, and text draft before submit.
- Move state to `Route` or ViewModel when it must survive process recreation, screen return, repository sync, or business rules.
- If state affects navigation, persistence, network/database calls, or cross-screen behavior, do not keep it only in `Screen`.

## MVI/UDF
- ViewModel state is a single immutable state object.
- User actions enter the ViewModel as intents or explicit event functions.
- One-off events such as navigation and snackbars are effects.
- Composables must not contain business logic.

## UI Implementation
- Prefer existing design system theme colors, typography, spacing, and components.
- Put common branding resources in `:core:designsystem`.
- Put feature-only resources in the owning feature module.
- Keep previews easy by making `Screen` callable without Android framework objects.
- Use clear callback names such as `onBackClick`, `onPrimaryClick`, `onComplete`, and `onSkip`.

## Practical Rule
Default to stateless screens, but do not force every pixel of UI state upward. Lift state only when ownership, persistence, testing, or business behavior needs it.
