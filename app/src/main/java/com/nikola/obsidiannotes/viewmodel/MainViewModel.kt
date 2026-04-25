package com.nikola.obsidiannotes.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.obsidiannotes.data.AppDatabase
import com.nikola.obsidiannotes.data.NoteEntity
import com.nikola.obsidiannotes.data.SyncManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val syncManager = SyncManager(application, database.noteDao())
    
    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes

    private val _selectedFolderUri = MutableStateFlow<Uri?>(null)
    val selectedFolderUri: StateFlow<Uri?> = _selectedFolderUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            _searchQuery
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        database.noteDao().getAllNotes()
                    } else {
                        database.noteDao().searchNotes("*$query*")
                    }
                }
                .collect {
                    _notes.value = it
                }
        }
    }

    fun setFolderUri(uri: Uri) {
        _selectedFolderUri.value = uri
        syncAndLoad()
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun syncAndLoad() {
        val uri = _selectedFolderUri.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                syncManager.syncDirectory(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
