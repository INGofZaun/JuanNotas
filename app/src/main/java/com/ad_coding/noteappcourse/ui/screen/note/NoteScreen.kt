@file:OptIn(ExperimentalMaterial3Api::class)

package com.ad_coding.noteappcourse.ui.screen.note

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.io.File
import android.graphics.BitmapFactory
import coil.compose.rememberImagePainter
<<<<<<< HEAD
=======


>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    estadoFecha: EstadoFecha,
    alarmScheduler: AlarmScheduler,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
<<<<<<< HEAD
    navController: NavController
=======
    navController: NavController,
    viewModel: NoteViewModel // Asegúrate de aceptar el viewModel aquí
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 15.dp, vertical = 15.dp),
        ) {
            // Aquí pasamos el viewModel a AudioRecorderButton
            AudioRecorderButton(viewModel = viewModel)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MultimediaPicker(onEvent)
                CameraButton { uri ->
<<<<<<< HEAD
                    onEvent(NoteEvent.AddMultimedia(uri.toString()))
                }
                DatePickerFecha(estadoFecha, onEvent)
            }
            AudioRecorderButton { audioUri ->
                onEvent(NoteEvent.AddMultimedia(audioUri))  // Pasar el URI al ViewModel
            }
=======
                    // Esta es la función que se ejecutará cuando se capture un multimedia
                    onEvent(NoteEvent.AddMultimedia(uri))
                }
                DatePickerFecha(estadoFecha, onEvent)
            }
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36

            /// Combina las listas multimedia y multimediaTemp
            val allMultimedia = state.multimedia + state.multimediaTemp

            // Mostrar multimedia guardada (incluyendo la grabada)
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
                            multimediaUri.startsWith("file://") -> {
<<<<<<< HEAD
                                // Para imágenes tomadas con la cámara
=======
                                // Para imágenes capturadas con la cámara
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
                                val file = File(multimediaUri.removePrefix("file://"))
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                                androidx.compose.foundation.Image(
                                    painter = rememberImagePainter(bitmap),
                                    contentDescription = "Imagen de la nota",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                        .clickable {
                                            navController.navigate("media_viewer/${Uri.encode(multimediaUri)}")
                                        }
                                )
                            }
                            multimediaUri.startsWith("content://media/external/images") -> {
                                // Para imágenes de la galería
                                androidx.compose.foundation.Image(
                                    painter = rememberAsyncImagePainter(model = multimediaUri),
                                    contentDescription = "Imagen de la galería",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                        .clickable {
                                            navController.navigate("media_viewer/${Uri.encode(multimediaUri)}")
                                        }
                                )
                            }
                            multimediaUri.startsWith("content://media/external/video") -> {
                                // Para videos
                                Icon(
                                    imageVector = Icons.Filled.Movie,
                                    contentDescription = "Video",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            navController.navigate("media_viewer/${Uri.encode(multimediaUri)}")
                                        }
                                )
                            }
                            multimediaUri.startsWith("content://media/external/audio") || multimediaUri.endsWith(".3gp") -> {
                                // Para audios (se incluye una extensión común para grabaciones de voz, por ejemplo)
                                Icon(
                                    imageVector = Icons.Filled.AudioFile,
                                    contentDescription = "Audio",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            navController.navigate("media_viewer/${Uri.encode(multimediaUri)}")
                                        }
                                )
                            }
                        }
                    }
                }
            }

<<<<<<< HEAD

=======
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
                            multimediaUri.startsWith("file://") -> {
                                val file = File(multimediaUri.removePrefix("file://"))
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                                androidx.compose.foundation.Image(
                                    painter = rememberImagePainter(bitmap),
                                    contentDescription = "Nueva imagen",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                )
                            }
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
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36

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

<<<<<<< HEAD
=======




@Composable
fun onMediaClick(uri: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(uri), getMimeType(uri))
    }
    LocalContext.current.startActivity(intent)
}

>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
// Determinar el tipo MIME del archivo
fun getMimeType(uri: String): String {
    return when {
        uri.endsWith(".jpg") || uri.endsWith(".jpg") -> "image/*"
        uri.endsWith(".mp4") -> "video/*"
        uri.endsWith(".mp3") -> "audio/*"
        else -> "*/*"
    }
}

