package com.ad_coding.noteappcourse.ui.screen.note

import android.net.Uri

sealed class NoteEvent {
    data class TitleChange(val value: String) : NoteEvent()
    data class ContentChange(val value: String) : NoteEvent()
    data class TipoCambio(val value: String) : NoteEvent()
    data class FechaCambio(val value: String) : NoteEvent()
    data class MultimediaCambio(val value: List<String>) : NoteEvent() // Cambios en multimedia
    object Save : NoteEvent()
    object DeleteNote : NoteEvent()
    object NavigateBack : NoteEvent()
    data class MultimediaDeCamera(val uri: String) : NoteEvent()
    data class AddMultimedia(val uri: String) : NoteEvent()
}
