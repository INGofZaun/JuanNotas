package com.ad_coding.noteappcourse.ViewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EstadoFecha : ViewModel() {
    var estadoFecha by mutableStateOf<String?>(null) // Usa null como valor predeterminado
}
