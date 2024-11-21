package com.ad_coding.noteappcourse

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ad_coding.noteappcourse.Alarma.AlarmSchedulerImpl
import com.ad_coding.noteappcourse.ViewModel.EstadoFecha
import com.ad_coding.noteappcourse.ui.screen.note.NoteScreen
import com.ad_coding.noteappcourse.ui.screen.note.NoteViewModel
import com.ad_coding.noteappcourse.ui.screen.note_list.NoteListScreen
import com.ad_coding.noteappcourse.ui.screen.note_list.NoteListViewModel
import com.ad_coding.noteappcourse.ui.theme.NoteAppCourseTheme
import com.ad_coding.noteappcourse.ui.util.Route
import com.ad_coding.noteappcourse.ui.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import android.content.SharedPreferences
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppCourseTheme {
                val navController = rememberNavController()
                val estadoFecha: EstadoFecha by viewModels()
                NavHost(
                    navController = navController,
                    startDestination = Route.noteList
                ) {
                    composable(route = Route.noteList) {
                        val viewModel = hiltViewModel<NoteListViewModel>()
                        val noteList by viewModel.noteList.collectAsStateWithLifecycle()

                        NoteListScreen(
                            noteList = noteList,
                            onNoteClick = {
                                navController.navigate(
                                    Route.note.replace(
                                        "{id}",
                                        it.id.toString()
                                    )
                                )
                            },
                            onAddNoteClick = {
                                navController.navigate(Route.note)
                            }
                        )
                    }

                    composable(route = Route.note) {
                        val viewModel = hiltViewModel<NoteViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = true) {
                            viewModel.event.collect { event ->
                                when (event) {
                                    is UiEvent.NavigateBack -> {
                                        navController.popBackStack()
                                    }

                                    else -> Unit
                                }
                            }
                        }
                        NoteScreen(
                            estadoFecha,
                            alarmScheduler = AlarmSchedulerImpl(applicationContext),
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
        // Inicializa sharedPreferences
        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        // Verificar y solicitar permisos de notificación al abrir la app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            showNotification()
        }
    }

    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        } else {
            showNotification()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // El permiso fue concedido
                showNotification()
            } else {
                // El permiso fue denegado, incrementar el contador de denegaciones
                val deniedCount = sharedPreferences.getInt("notification_permission_denied_count", 0)
                sharedPreferences.edit().putInt("notification_permission_denied_count", deniedCount + 1).apply()
            }
        }
    }

    private fun showNotification() {
        // Crea un Intent para abrir la MainActivity cuando se hace clic en la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Puedes agregar datos adicionales para abrir una nota específica si lo deseas
            // putExtra("note_id", idNotaEspecifica)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Crear el canal de notificación si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "app_notifications"
            val channelName = "App Notifications"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val notification = NotificationCompat.Builder(this, "app_notifications")
            .setSmallIcon(R.drawable.ic_notification) // Reemplaza con tu propio ícono
            .setContentTitle("Bienvenido a la App de Notas")
            .setContentText("Gracias por usar nuestra app. ¡Explora tus notas!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Configura el PendingIntent
            .setAutoCancel(true) // Cancela la notificación al hacer clic
            .build()

        // Mostrar la notificación
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val NOTIFICATION_ID = 1
    }
}
