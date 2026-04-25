# AI Handover Context & Technical Architecture 🤖

## 🎯 Цель проекта
Создание Android-клиента для Obsidian-like заметок с упором на локальное хранение и системные уведомления.

## 🏗 Архитектурные паттерны
- **Pattern:** MVVM (Model-View-ViewModel).
- **UI:** 100% Jetpack Compose.
- **Data Flow:** Unidirectional Data Flow (UDF).
- **Concurrency:** Kotlin Coroutines & Flow.

## 📂 Технические детали (для разработчика)
1. **File System (SAF):**
   - Используется `Storage Access Framework`. Корневой URI должен храниться в `SharedPreferences` или `DataStore`.
   - Пути `notes/` и `.archive/` являются настраиваемыми.
2. **YAML Frontmatter:**
   - Парсинг: `org.yaml.snakeyaml.Yaml`.
   - Регулярка для разделения: `^---\s*\n(.*?)\n---\s*\n(.*)`.
3. **Markdown Rendering:**
   - Библиотека: `Markwon`.
   - Обёртка для Compose: `com.nikola.obsidiannotes.ui.components.MarkdownText`.
4. **Уведомления:**
   - `NotificationChannel ID: "pinned_notes_channel"`.
   - `Notification ID: note.fileName.hashCode()`.
   - Флаг: `Notification.FLAG_ONGOING_EVENT`.

## 🛠 Предстоящие задачи (Roadmap для ИИ)
- [ ] Добавить `FolderPicker` через SAF.
- [ ] Реализовать `NoteViewModel` для загрузки списка файлов.
- [ ] Интегрировать `Markwon` в основной экран просмотра.
- [ ] Добавить экран настроек для смены путей папок.

## ⚠️ Критически важные ограничения
- **Запрещено** использовать SQLite/Room как основное хранилище текстов. Только файлы.
- **Запрещено** удалять блок метаданных при перезаписи файла.
- **Обязательно** сохранять кодировку UTF-8.
