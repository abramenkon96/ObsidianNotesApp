package com.nikola.obsidiannotes.data

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4(contentEntity = NoteEntity::class)
@Entity(tableName = "notes_fts")
data class NoteFtsEntity(
    val title: String,
    val content: String
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val rowid: Int,
    val fileName: String,
    val title: String,
    val groupName: String,
    val color: String,
    val content: String,
    val isPinned: Boolean,
    val isArchived: Boolean,
    val lastModified: Long
)
