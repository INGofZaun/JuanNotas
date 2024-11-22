@file:OptIn(ExperimentalMaterial3Api::class)

package com.ad_coding.noteappcourse.ui.screen.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ad_coding.noteappcourse.Alarma.AlarmItem
import com.ad_coding.noteappcourse.Alarma.AlarmScheduler
import com.ad_coding.noteappcourse.ViewModel.EstadoFecha
import com.ad_coding.noteappcourse.componentes.AudioRecorderButton
import com.ad_coding.noteappcourse.componentes.BotonD
import com.ad_coding.noteappcourse.componentes.CameraButton
import com.ad_coding.noteappcourse.componentes.DatePickerFecha
import com.ad_coding.noteappcourse.componentes.MultimediaPicker
import androidx.compose.material.icons.filled.Movie
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState



@OptIn(ExperimentalMaterial3Api::class)
// Agregar una sección en NoteScreen para mostrar multimedia
@Composable
fun NoteScreen(
    estadoFecha: EstadoFecha,
    alarmScheduler: AlarmScheduler,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "NUEVO") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NoteEvent.NavigateBack) }, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "REGRESAR")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(NoteEvent.DeleteNote) }, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "BORRAR")
                    }
                }
            )
        }
    ) { padding ->
        // Hacer que el contenido sea desplazable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Habilitar scroll vertical
                .padding(horizontal = 15.dp, vertical = 15.dp),
        ) {
            BotonD()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MultimediaPicker(onEvent)
                CameraButton()
                DatePickerFecha(estadoFecha, onEvent)
            }
            AudioRecorderButton()

            // Mostrar multimedia guardada
            if (state.multimedia.isNotEmpty()) {
                Text(text = "Multimedia Guardada:")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.multimedia) { multimediaUri ->
                        when {
                            multimediaUri.startsWith("content://media/external/images") -> {
                                androidx.compose.foundation.Image(
                                    painter = rememberAsyncImagePainter(model = multimediaUri),
                                    contentDescription = "Imagen de la nota",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                )
                            }
                            multimediaUri.startsWith("content://media/external/video") -> {
                                Icon(
                                    imageVector = Icons.Filled.Movie,
                                    contentDescription = "Video",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Mostrar multimedia temporal
            if (state.multimediaTemp.isNotEmpty()) {
                Text(text = "Multimedia Temporal:")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.multimediaTemp) { multimediaUri ->
                        when {
                            multimediaUri.startsWith("content://media/external/images") -> {
                                androidx.compose.foundation.Image(
                                    painter = rememberAsyncImagePainter(model = multimediaUri),
                                    contentDescription = "Nueva imagen",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                )
                            }
                            multimediaUri.startsWith("content://media/external/video") -> {
                                Icon(
                                    imageVector = Icons.Filled.Movie,
                                    contentDescription = "Nuevo video",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Campos de texto
            OutlinedTextField(
                value = state.title,
                onValueChange = { onEvent(NoteEvent.TitleChange(it)) },
                placeholder = { Text(text = "TÍTULO") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )
            OutlinedTextField(
                value = state.content,
                onValueChange = { onEvent(NoteEvent.ContentChange(it)) },
                placeholder = { Text(text = "CONTENIDO") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Botón Guardar al final
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Guardar la nota y multimedia
                        onEvent(NoteEvent.Save)
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "GUARDAR")
                }
            }
        }
    }
}
