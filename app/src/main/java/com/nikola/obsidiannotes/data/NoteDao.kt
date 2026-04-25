package com.nikola.obsidiannotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isArchived = 0 ORDER BY lastModified DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes JOIN notes_fts ON notes.rowid = notes_fts.rowid WHERE notes_fts MATCH :query AND isArchived = 0")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("DELETE FROM notes WHERE fileName = :fileName")
    suspend fun deleteByFileName(fileName: String)

    @Query("SELECT * FROM notes WHERE fileName = :fileName LIMIT 1")
    suspend fun getNoteByFileName(fileName: String): NoteEntity?
    
    @Query("SELECT fileName, lastModified FROM notes")
    suspend fun getAllTimestamps(): List<NoteTimestamp>
}

data class NoteTimestamp(val fileName: String, val lastModified: Long)
