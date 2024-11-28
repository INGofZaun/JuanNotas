package com.ad_coding.noteappcourse.ui.screen.note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad_coding.noteappcourse.domain.model.Note
import com.ad_coding.noteappcourse.domain.repository.NoteRepository
import com.ad_coding.noteappcourse.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    private val _event = Channel<UiEvent>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    // Función para clasificar el tipo de multimedia
    private fun classifyMultimediaType(uri: String): String {
        return when {
            uri.startsWith("content://media/external/images") -> "image"
            uri.startsWith("content://media/external/video") -> "video"
            uri.startsWith("content://media/external/audio") -> "audio"
            else -> "unknown"
        }
    }

    init {
        savedStateHandle.get<String>("id")?.let {
            val id = it.toInt()
            viewModelScope.launch {
                // Obtener los datos de la nota y multimedia
                repository.getNoteById(id)?.let { note ->
                    val multimediaUris = repository.getMultimediaForNote(id)
                    Log.d("NoteViewModel", "Multimedia para la nota $id: $multimediaUris")
                    _state.update { screenState ->
                        screenState.copy(
                            id = note.id,
                            title = note.title,
                            content = note.content,
                            tipo = note.tipo,
                            fecha = note.fecha,
                            multimedia = multimediaUris,
                            multimediaTemp = emptyList() // Limpia la multimedia temporal
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.ContentChange -> {
                _state.update { it.copy(content = event.value) }
            }

            is NoteEvent.TipoCambio -> {
                _state.update { it.copy(tipo = event.value) }
            }

            is NoteEvent.MultimediaCambio -> {
                // Agrega multimedia temporal
                _state.update { it.copy(multimediaTemp = it.multimediaTemp + event.value) }
            }

            is NoteEvent.FechaCambio -> {
                _state.update { it.copy(fecha = event.value) }
            }

            is NoteEvent.TitleChange -> {
                _state.update { it.copy(title = event.value) }
            }

            is NoteEvent.MultimediaDeCamera -> {
                // Clasificar y agregar la multimedia tomada con la cámara a multimediaTemp
                val multimediaUri = event.uri.toString()
                val multimediaType = classifyMultimediaType(multimediaUri)
                Log.d("NoteViewModel", "Multimedia tipo: $multimediaType, URI: $multimediaUri")
                _state.update { it.copy(multimediaTemp = it.multimediaTemp + multimediaUri) }
            }

            is NoteEvent.AddMultimedia -> {
                // Clasificar y agregar multimedia desde el evento AddMultimedia
                val multimediaUri = event.uri.toString()
                val multimediaType = classifyMultimediaType(multimediaUri)
                Log.d("NoteViewModel", "Multimedia tipo: $multimediaType, URI: $multimediaUri")
                _state.update { it.copy(multimediaTemp = it.multimediaTemp + multimediaUri) }
            }

            NoteEvent.NavigateBack -> sendEvent(UiEvent.NavigateBack)

            NoteEvent.Save -> {
                viewModelScope.launch {
                    val state = state.value
                    val note = Note(
                        id = state.id,
                        title = state.title,
                        content = state.content,
                        tipo = state.tipo,
                        fecha = state.fecha,
                        multimedia = state.multimedia + state.multimediaTemp // Combina multimedia
                    )
                    if (state.id == null) {
                        repository.insertNote(note)
                        Log.d("NoteViewModel", "Note inserted successfully.")
                    } else {
                        repository.updateNote(note)
                        Log.d("NoteViewModel", "Note updated successfully.")
                    }
                    // Limpia la multimedia temporal después de guardar
                    _state.update { it.copy(multimediaTemp = emptyList()) }
                    sendEvent(UiEvent.NavigateBack)
                }
            }

            NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    val state = state.value
                    val note = Note(
                        id = state.id,
                        title = state.title,
                        content = state.content,
                        tipo = state.tipo,
                        fecha = state.fecha,
                        multimedia = state.multimedia
                    )
                    repository.deleteNote(note)
                    Log.d("NoteViewModel", "Note deleted successfully.")
                    sendEvent(UiEvent.NavigateBack)
                }
            }
        }
    }
}

