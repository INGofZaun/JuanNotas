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

    init {
        savedStateHandle.get<String>("id")?.let {
            val id = it.toInt()
            viewModelScope.launch {
                repository.getNoteById(id)?.let { note ->
                    _state.update { screenState ->
                        screenState.copy(
                            id = note.id,
                            title = note.title,
                            content = note.content,
                            tipo = note.tipo,
                            fecha= note.fecha,
                            foto = note.foto
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEvent) {
        var listaU : List<String> = listOf()
        when (event) {
            is NoteEvent.ContentChange -> {
                _state.update {
                    it.copy(
                        content = event.value
                    )
                }
            }
            is NoteEvent.TipoCambio -> {
            _state.update {
                it.copy(
                    tipo = event.value
                )
            }
        }
            is NoteEvent.FotoCambio -> {
                listaU = event.value
            }
            is NoteEvent.FechaCambio -> {
                _state.update {
                    it.copy(
                        fecha = event.value
                    )
                }
            }

            is NoteEvent.TitleChange -> {
                _state.update {
                    it.copy(
                        title = event.value
                    )
                }
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
                        fecha= state.fecha,
                        foto = state.foto
                    )
                    if (state.id == null) {
                        val id = repository.insertNote(note)
                        //listaU.forEach{uri->
                          //  repository.insertFoto(id,uri)
                        //}
                        Log.d("-------------------------","SI JALOOO"+id)
                    } else {
                        repository.updateNote(note)
                    }

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
                        fecha= state.fecha,
                        foto = state.foto
                    )
                    repository.deleteNote(note)
                }
                sendEvent(UiEvent.NavigateBack)
            }
        }
    }
}