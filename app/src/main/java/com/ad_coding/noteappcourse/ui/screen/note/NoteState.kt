package com.ad_coding.noteappcourse.ui.screen.note

data class NoteState(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val tipo: String = "",
    val fecha: String = "",
    val multimedia: List<String> = emptyList(), // Multimedia persistente
    val multimediaTemp: List<String> = emptyList() // Multimedia temporal
)
