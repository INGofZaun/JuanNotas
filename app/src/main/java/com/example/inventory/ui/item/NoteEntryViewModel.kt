package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar y guardar los datos de una nota nueva.
 */
class NoteEntryViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    // Estado de la UI para entrada de notas.
    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState: StateFlow<NoteUiState> = _noteUiState.asStateFlow()

    /**
     * Actualiza el título en el estado actual de la nota.
     */
    fun updateTitle(newTitle: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails.copy(title = newTitle),
            isEntryValid = validateInput(newTitle, _noteUiState.value.noteDetails.content)
        )
    }

    /**
     * Actualiza el contenido en el estado actual de la nota.
     */
    fun updateContent(newContent: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails.copy(content = newContent),
            isEntryValid = validateInput(_noteUiState.value.noteDetails.title, newContent)
        )
    }

    /**
     * Valida que el título y el contenido no estén vacíos.
     */
    private fun validateInput(title: String, content: String): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }

    /**
     * Guarda la nota en el repositorio si la entrada es válida.
     */
    fun saveNote() {
        if (_noteUiState.value.isEntryValid) {
            viewModelScope.launch {
                notesRepository.insertNote(_noteUiState.value.noteDetails.toNote())
            }
        }
    }
}

/**
 * Estado de la UI para la pantalla de entrada de notas.
 */
data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Clase para representar los detalles de una nota.
 */
data class NoteDetails(
    val id: Int = 0,
    val title: String = "",
    val content: String = ""
)

/**
 * Convierte un objeto `NoteDetails` a `Note`.
 */
fun NoteDetails.toNote(): Note = Note(
    id = id,
    title = title,
    content = content
)
