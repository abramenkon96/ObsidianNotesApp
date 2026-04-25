# Система версий проекта 🏷️

Мы используем **Semantic Versioning 2.0.0** для управления версиями приложения.

## Формат версии: `MAJOR.MINOR.PATCH`
- **MAJOR:** Значительные изменения архитектуры или несовместимые изменения метаданных.
- **MINOR:** Добавление функционала (новые экраны, интеграции) с сохранением обратной совместимости.
- **PATCH:** Исправление багов и мелкие правки.

## Как обновлять версию:
1. Обновите `versionName` в `app/build.gradle.kts`.
2. Увеличьте `versionCode` на +1.
3. Создайте **Tag** на GitHub: `git tag -a v1.0.0 -m "Release version 1.0.0"`.

## История версий (Changelog):
- **v1.0.0 (2024-04-26):**
    - Initial build system setup.
    - YAML Frontmatter parser core.
    - Pinned notifications core.
    - Basic Jetpack Compose UI.
    - CI/CD with GitHub Actions.
