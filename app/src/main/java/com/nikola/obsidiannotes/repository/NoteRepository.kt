package com.nikola.obsidiannotes.repository

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.nikola.obsidiannotes.model.Note
import com.nikola.obsidiannotes.util.NoteParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class NoteRepository(private val context: Context) {

    suspend fun loadNotesFromFolder(folderUri: Uri): List<Note> = withContext(Dispatchers.IO) {
        val notes = mutableListOf<Note>()
        val rootFolder = DocumentFile.fromTreeUri(context, folderUri) ?: return@withContext emptyList()
        
        // Ищем папку "notes" или работаем в корне (в зависимости от настроек)
        val notesFolder = rootFolder.findFile("notes") ?: rootFolder
        
        notesFolder.listFiles().forEach { file ->
            if (file.isFile && file.name?.endsWith(".md") == true) {
                val content = readFileContent(file.uri)
                val note = NoteParser.parse(
                    fileName = file.name ?: "unknown.md",
                    fullText = content,
                    lastModified = file.lastModified()
                )
                notes.add(note)
            }
        }
        return@withContext notes.sortedByDescending { it.lastModified }
    }

    private fun readFileContent(uri: Uri): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            inputStream?.close()
            stringBuilder.toString()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun saveNote(folderUri: Uri, note: Note) = withContext(Dispatchers.IO) {
        val rootFolder = DocumentFile.fromTreeUri(context, folderUri) ?: return@withContext
        val notesFolder = rootFolder.findFile("notes") ?: rootFolder
        
        var file = notesFolder.findFile(note.fileName)
        if (file == null) {
            file = notesFolder.createFile("text/markdown", note.fileName)
        }
        
        file?.let {
            try {
                context.contentResolver.openOutputStream(it.uri)?.use { outputStream ->
                    outputStream.write(note.toFileContent().toByteArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
