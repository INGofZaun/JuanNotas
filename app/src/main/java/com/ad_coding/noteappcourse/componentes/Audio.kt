package com.ad_coding.noteappcourse.componentes

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ad_coding.noteappcourse.ui.screen.note.NoteViewModel  // Agregado el import
import com.ad_coding.noteappcourse.ui.screen.note.NoteEvent  // Agregado el import
import java.io.File
import android.net.Uri

@Composable
<<<<<<< HEAD
fun AudioRecorderButton(onAudioRecorded: (String) -> Unit) {  // Agregar el callback
=======
fun AudioRecorderButton(viewModel: NoteViewModel) {
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    val mediaRecorder = remember { MediaRecorder() }
    val mediaPlayer = MediaPlayer()

    var audioFiles by remember { mutableStateOf(listOf<File>()) }

    val getNextAudioFile: () -> File = {
        File(context.externalCacheDir, "audio_recording_${audioFiles.size + 1}.3gp")
    }

    val startRecording = {
        val audioFile = getNextAudioFile()
        Log.d("AudioRecorder", "Intentando iniciar la grabación")
        try {
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(audioFile.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare()
                start()
            }
            isRecording = true
            audioFiles = audioFiles + audioFile
            Log.d("AudioRecorder", "Grabación iniciada")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al iniciar la grabación", e)
        }
    }

    val stopRecording = {
        Log.d("AudioRecorder", "Intentando detener la grabación")
        try {
            mediaRecorder.apply {
                stop()
                reset()
            }
            isRecording = false
<<<<<<< HEAD
            // Llamar al callback pasando el URI del archivo grabado
            audioFiles.lastOrNull()?.absolutePath?.let { onAudioRecorded(it) }
=======
            // Convertir el String URI a Uri antes de pasarlo al ViewModel
            val audioFileUri = audioFiles.last().toURI().toString()
            val uri = Uri.parse(audioFileUri)  // Conversión a Uri
            viewModel.onEvent(NoteEvent.AddMultimedia(uri))  // Ahora pasamos el Uri
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
            Log.d("AudioRecorder", "Grabación detenida")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al detener la grabación", e)
        }
    }

    val startPlaying = { audioFile: File ->
        try {
            mediaPlayer.apply {
                reset()
                setDataSource(audioFile.absolutePath)
                prepare()
                start()
                isPlaying = true
            }
            mediaPlayer.setOnCompletionListener {
                isPlaying = false
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error al reproducir el audio", e)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
        if (isGranted && !isRecording) {
            startRecording()
        }
    }

    Column {
        IconButton(onClick = {
            if (hasPermission) {
                if (isRecording) stopRecording() else startRecording()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }) {
            if (isRecording) {
                Icon(Icons.Filled.Stop, contentDescription = "Detener Grabación", tint = Color.Red)
            } else {
                Icon(Icons.Filled.Mic, contentDescription = "Iniciar Grabación", tint = Color.Black)
            }
        }

        audioFiles.forEachIndexed { index, audioFile ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Button(onClick = { startPlaying(audioFile) }) {
                    Text("Audio ${index + 1}")
                }
                IconButton(onClick = {
                    audioFiles = audioFiles.filter { it != audioFile }
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Audio", tint = Color.Red)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
            mediaRecorder.release()
        }
    }
}
<<<<<<< HEAD


=======
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
