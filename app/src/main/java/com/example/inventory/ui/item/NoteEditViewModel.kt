package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.inventory.data.NotesRepository

class NoteEditViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    var noteUiState by mutableStateOf(NoteUiState())
        private set

    fun updateTitle(newTitle: String) {
        noteUiState = noteUiState.copy(
            noteDetails = noteUiState.noteDetails.copy(title = newTitle)
        )
    }

    fun updateContent(newContent: String) {
        noteUiState = noteUiState.copy(
            noteDetails = noteUiState.noteDetails.copy(content = newContent)
        )
    }

    fun saveNote() {
        // Código para guardar la nota
    }
}
