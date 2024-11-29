package com.ad_coding.noteappcourse.componentes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream

@Composable
fun CameraButton(onMultimediaCaptured: (Uri) -> Unit) {
    var imageUris by remember { mutableStateOf<List<Uri>>(listOf()) } // Lista de URIs
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Obtener contexto desde composable
    val context = LocalContext.current

    val openCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToFile(context, bitmap) // Llamar a la función con contexto dentro de composable
            imageUris = imageUris + uri
            onMultimediaCaptured(uri)
        }
    }

    Column {
        Button(onClick = {
            openCamera.launch(null)
        }) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Abrir cámara")
        }

        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(imageUris) { uri ->
                // Mostrar las imágenes capturadas
                Image(
                    bitmap = bitmapFromUri(context, uri).asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clickable {
                            selectedUri = uri
                            showDeleteDialog = true
                        }
                )
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar imagen") },
                text = { Text("¿Deseas eliminar esta imagen?") },
                confirmButton = {
                    Button(onClick = {
                        showDeleteDialog = false
                        selectedUri?.let { uri ->
                            imageUris = imageUris.filter { it != uri }
                        }
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

// Función para convertir Bitmap a URI (si necesitas almacenar como archivo)
fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return Uri.fromFile(file)
}

// Función para obtener un Bitmap desde una URI
fun bitmapFromUri(context: Context, uri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}
