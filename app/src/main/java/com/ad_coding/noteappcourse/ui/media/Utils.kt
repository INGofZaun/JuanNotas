package com.ad_coding.noteappcourse.ui.media

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

fun getMimeType(context: Context, uri: String): String? {
    val contentResolver = context.contentResolver
    val type = contentResolver.getType(Uri.parse(uri))
    if (type != null) return type

    val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.parse(uri).toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}
