# Wow Effect Architecture Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement Room FTS database, SAF synchronization, and basic UI for instant search.

**Architecture:** Room FTS acting as a fast read-cache, synchronized with SAF directory contents on resume.

**Tech Stack:** Kotlin, Jetpack Compose, Room (FTS4), Android Storage Access Framework.

---

### Task 1: Add Dependencies

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Add Room and Coroutines dependencies to toml**
Update `libs.versions.toml` to include room versions.

- [ ] **Step 2: Apply Room and KSP to app module**
Update `app/build.gradle.kts` to apply the KSP plugin and add Room dependencies.

### Task 2: Create Room Database and Entity

**Files:**
- Create: `app/src/main/java/com/nikola/obsidiannotes/data/NoteEntity.kt`
- Create: `app/src/main/java/com/nikola/obsidiannotes/data/NoteDao.kt`
- Create: `app/src/main/java/com/nikola/obsidiannotes/data/AppDatabase.kt`

- [ ] **Step 1: Create NoteEntity with FTS4**
Write the data class representing the cached note.

- [ ] **Step 2: Create NoteDao**
Write SQL queries for inserting, deleting, and full-text searching notes.

- [ ] **Step 3: Create AppDatabase**
Set up the RoomDatabase instance.

### Task 3: Implement SAF and Sync Logic

**Files:**
- Create: `app/src/main/java/com/nikola/obsidiannotes/data/SyncManager.kt`
- Modify: `app/src/main/java/com/nikola/obsidiannotes/MainActivity.kt`

- [ ] **Step 1: Create SyncManager**
Implement the logic to read `lastModified` from the SAF DocumentFile tree and update the Room DB.

- [ ] **Step 2: Wire up SAF picker in MainActivity**
Add the `ActivityResultContracts.OpenDocumentTree()` to select the root folder and trigger SyncManager.

### Task 4: UI Updates

**Files:**
- Modify: `app/src/main/java/com/nikola/obsidiannotes/MainActivity.kt`

- [ ] **Step 1: Connect DB to Compose UI**
Observe notes from Room instead of the mock state. Add a search bar.
