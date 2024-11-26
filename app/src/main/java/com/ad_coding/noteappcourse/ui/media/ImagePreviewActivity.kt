package com.ad_coding.noteappcourse.ui.screen.note

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import android.content.Context
import android.webkit.MimeTypeMap
import androidx.compose.ui.platform.LocalContext


@Composable
fun MediaViewerScreen(uri: String) {
    val context = LocalContext.current
    val mimeType = getMimeType(context, uri)

    when {
        mimeType?.startsWith("image/") == true -> {
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Imagen en detalle",
                modifier = Modifier.fillMaxSize()
            )
        }
        mimeType?.startsWith("video/") == true -> {
            AndroidView(factory = { context ->
                VideoView(context).apply {
                    setVideoURI(Uri.parse(uri))
                    start()
                }
            }, modifier = Modifier.fillMaxSize())
        }
        mimeType?.startsWith("audio/") == true -> {
            Text("Reproduciendo audio: $uri")
            // Aquí puedes integrar un reproductor de audio si es necesario.
        }
        else -> {
            Text("Formato de archivo no compatible o desconocido.")
        }
    }
}



fun getMimeType(context: Context, uri: String): String? {
    val contentResolver = context.contentResolver
    val type = contentResolver.getType(Uri.parse(uri))
    if (type != null) return type

    val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.parse(uri).toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}


