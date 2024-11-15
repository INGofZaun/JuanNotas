package com.ad_coding.noteappcourse.componentes

import android.graphics.Bitmap
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
import androidx.compose.ui.unit.dp

@Composable
fun CameraButton() {
    var imageBitmaps by remember { mutableStateOf<List<Bitmap>>(listOf()) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val openCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {

            imageBitmaps = imageBitmaps + bitmap
        }
    }

    Column {
        Button(onClick = {
            openCamera.launch(null)
        }) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Abrir cámara")
        }

        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(imageBitmaps) { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clickable {
                            selectedBitmap = bitmap
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
                        selectedBitmap?.let { bitmap ->
                            imageBitmaps = imageBitmaps.filter { it != bitmap }
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

