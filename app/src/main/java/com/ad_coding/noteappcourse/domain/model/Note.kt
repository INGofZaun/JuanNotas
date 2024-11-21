package com.ad_coding.noteappcourse.domain.model

data class Note(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val tipo : String = "",
    val fecha : String ="",
    val foto : String=""
)
