 package com.ad_coding.noteappcourse.componentes

 import android.net.Uri
 import android.app.Activity
 import android.content.Intent
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
 import androidx.compose.material.icons.filled.PermMedia
 import androidx.compose.material3.AlertDialog
 import androidx.compose.material3.Button
 import androidx.compose.material3.Icon
 import androidx.compose.material3.Text
 import androidx.compose.runtime.*
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.asImageBitmap
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import coil.compose.rememberAsyncImagePainter
 import com.ad_coding.noteappcourse.ui.screen.note.NoteEvent

 @Composable
 fun MultimediaPicker(
     onEvent: (NoteEvent) -> Unit,
 ) {
     val context = LocalContext.current
     var multimediaUris by remember { mutableStateOf<List<String>>(listOf()) }
     var selectedUri by remember { mutableStateOf<Uri?>(null) }
     var showDialogForSelection by remember { mutableStateOf(false) }
     var showDeleteDialog by remember { mutableStateOf(false) }

     // Inicializar el ActivityResultLauncher
     val multimediaPickerLauncher = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.StartActivityForResult()
     ) { result ->
         if (result.resultCode == Activity.RESULT_OK) {
             // Obtener las URIs de los archivos seleccionados
             val clipData = result.data?.clipData
             if (clipData != null) {
                 val uris = mutableListOf<String>()
                 for (i in 0 until clipData.itemCount) {
                     uris.add(clipData.getItemAt(i).uri.toString())

                 }
                 multimediaUris = uris
                 onEvent(NoteEvent.FotoCambio(multimediaUris))


             } else {
                 result.data?.data?.let { uri ->
                     multimediaUris = listOf()
                 }
             }
         }
     }

     Column {
         Button(onClick = {
             showDialogForSelection = true
         }) {
             Icon(Icons.Filled.PermMedia, contentDescription = "Seleccionar Multimedia")
         }

         LazyColumn(modifier = Modifier.height(150.dp)) {
             items(multimediaUris) { uri ->
                 Image(
                     painter = rememberAsyncImagePainter(model = Uri.parse(uri)),
                     contentDescription = null,
                     modifier = Modifier
                         .width(100.dp)
                         .height(150.dp)
                         .clickable {
                             selectedUri = Uri.parse(uri)
                             showDeleteDialog = true
                         }
                 )
             }
         }

         if (showDialogForSelection) {
             AlertDialog(
                 onDismissRequest = { showDialogForSelection = false },
                 title = { Text(text = "Estás seguro de abir la Multimedia?") },
                 confirmButton = {
                     Button(onClick = {
                         showDialogForSelection = false
                         val intent = Intent(Intent.ACTION_PICK).apply {
                             type = "image/* video/*"
                             putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                         }
                         multimediaPickerLauncher.launch(intent)
                     }) {
                         Text("Aceptar")
                     }
                 },
                 dismissButton = {
                     Button(onClick = { showDialogForSelection = false }) {
                         Text("Cancelar")
                     }
                 }
             )
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
                             multimediaUris = multimediaUris.filter {  Uri.parse(it) != uri }
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
