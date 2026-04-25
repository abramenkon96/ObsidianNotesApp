# Android Markdown Notes: "Wow Effect" Architecture Design

## 1. Goal
Create a premium Android markdown notes application that integrates directly with the file system (via SAF) while providing lightning-fast search and fluid UI transitions. The goal is to deliver a AAA "Wow" experience akin to Obsidian or Notion.

## 2. Architecture & Data Flow
- **Source of Truth (SoT):** Local `.md` files accessed via Storage Access Framework (SAF).
- **Read-Cache:** Room SQLite database with FTS4/FTS5 (Full-Text Search). The database stores the full content and metadata of notes *only* for fast UI rendering and searching.
- **Sync Mechanism (External Changes):** 
  - **On-Resume Fast Sync:** When the app comes to the foreground, a coroutine scans the SAF directory for `lastModified` timestamps. Any file newer than the Room cache is parsed and updated in the DB in the background.
  - **ContentObserver:** A listener on the SAF root URI detects real-time file system changes made by external apps.
- **Write Flow:** UI Edit -> Save to `.md` file via SAF -> Update Room Cache.

## 3. UI Components & "Wow" Features
- **Dashboard:**
  - `LazyColumn` for notes list.
  - Sticky headers for grouping (e.g., "Projects", "Personal").
  - Instant live search bar filtering via Room FTS.
  - Shared Element Transitions: Tapping a note smoothly expands the card into the full-screen editor.
- **Editor / Viewer:**
  - **Dual Mode:** Markwon library for rich Markdown rendering (including interactive checkboxes `- [ ]`).
  - **Seamless Switch:** Double-tap `Modifier.combinedClickable` to instantly switch between reading and editing modes.
- **Notifications:**
  - Foreground Service to keep pinned notes persistently in the notification shade.

## 4. Error Handling
- **File Access Failure:** Show a `Snackbar` and disable editing for that specific note.
- **Corrupted YAML Frontmatter:** Fallback to safe defaults (e.g., title = filename, group = "Uncategorized").

## 5. Testing Strategy
- Unit tests for the YAML parsing logic (`NoteParser`).
- Instrumental tests for Room DB read/write/FTS queries.
