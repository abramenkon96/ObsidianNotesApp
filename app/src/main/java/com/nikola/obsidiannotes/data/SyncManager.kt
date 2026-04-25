package com.nikola.obsidiannotes.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.nikola.obsidiannotes.util.NoteParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class SyncManager(
    private val context: Context,
    private val noteDao: NoteDao
) {
    suspend fun syncDirectory(treeUri: Uri) = withContext(Dispatchers.IO) {
        val rootDir = DocumentFile.fromTreeUri(context, treeUri) ?: return@withContext
        val files = rootDir.listFiles().filter { it.name?.endsWith(".md") == true }
        
        val dbTimestamps = noteDao.getAllTimestamps().associateBy({ it.fileName }, { it.lastModified })
        val currentFileNames = mutableSetOf<String>()

        for (file in files) {
            val fileName = file.name ?: continue
            val lastModified = file.lastModified()
            currentFileNames.add(fileName)

            val dbLastModified = dbTimestamps[fileName]
            
            // Если файла нет в БД или он был изменен
            if (dbLastModified == null || lastModified > dbLastModified) {
                try {
                    context.contentResolver.openInputStream(file.uri)?.use { inputStream ->
                        val content = InputStreamReader(inputStream).readText()
                        val note = NoteParser.parse(fileName, content, lastModified)
                        
                        val entity = NoteEntity(
                            rowid = note.fileName.hashCode(),
                            fileName = note.fileName,
                            title = note.title,
                            groupName = note.group,
                            color = note.color,
                            content = note.content,
                            isPinned = note.isPinned,
                            isArchived = note.isArchived,
                            lastModified = note.lastModified
                        )
                        noteDao.insert(entity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Удаляем из БД те, что были удалены с диска
        val deletedFiles = dbTimestamps.keys - currentFileNames
        for (deletedFileName in deletedFiles) {
            noteDao.deleteByFileName(deletedFileName)
        }
    }
}
