# Codex Preflight

Use this before implementing or modifying code in this repository.

## Always Check
- Read the user request and the newest conversation context first.
- Check `git status --short --untracked-files=all` before editing.
- Do not revert or overwrite user changes.
- Prefer existing module patterns over new abstractions.
- Keep changes scoped to the requested feature or bug.

## Read Before Editing
- For Compose screen work, read `docs/compose-screen-conventions.md`.
- For new modules, navigation wiring, resources, or Gradle wiring, read `docs/module-conventions.md`.
- For architecture direction, read `ARCHITECTURE.md`.
- For project-wide rules, read `RULES.md`.

## Validation
- Run the smallest meaningful Gradle task for the changed surface.
- For feature UI/module work, prefer:
  - `./gradlew :feature:<name>:assembleDebug`
  - `./gradlew :app:assembleDebug`
- Report build warnings only when they are new, relevant, or actionable.
