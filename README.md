# Obsidian Notes for Android 📱📝

[![Android CI Build](https://github.com/abramenkon96/ObsidianNotesApp/actions/workflows/android.yml/badge.svg)](https://github.com/abramenkon96/ObsidianNotesApp/actions/workflows/android.yml)

**Obsidian Notes** — это мощное, приватное и гибкое приложение для ежедневных заметок на Android. Оно разработано специально для тех, кто ценит формат Markdown и хочет иметь полный контроль над своими данными.

## 🌟 Основные преимущества
- **File-First:** Приложение работает напрямую с вашими `.md` файлами. Никаких закрытых баз данных.
- **Obsidian Sync:** Полная совместимость с метаданными (YAML Frontmatter).
- **Pinned Notifications:** Закрепляйте важные заметки в шторке уведомлений — они всегда под рукой и их нельзя смахнуть случайно.
- **Dynamic UI:** Переключение между режимом чтения и редактирования двойным кликом.
- **Smart Archive:** Удобная система архивации заметок вместо удаления.

## 🚀 Как начать
1. Скачайте последний билд из раздела [GitHub Actions](https://github.com/abramenkon96/ObsidianNotesApp/actions).
2. Выберите корневую папку для ваших заметок.
3. Настройте пути для `/notes` и `/.archive` в настройках.

## 🛠 Технологический стек
- **Kotlin 2.0** + **Jetpack Compose**
- **Architecture:** MVVM + Clean Architecture
- **Markdown:** Markwon Engine with Tasks & Tables support
- **Metadata:** SnakeYAML for Frontmatter parsing
- **CI/CD:** GitHub Actions for automated APK builds

## 📁 Структура метаданных
Каждый файл содержит YAML заголовок для синхронизации настроек между устройствами:
```yaml
---
title: "Моя заметка"
group: "Работа"
color: "#FF5733"
pinned: true
status: "active"
---
```

## 🤝 Контрибьютинг
Мы приветствуем вклад в проект! Если вы хотите улучшить приложение:
1. Сделайте Fork репозитория.
2. Создайте Feature Branch.
3. Отправьте Pull Request.

---
*Created with ❤️ for the Obsidian Community.*
