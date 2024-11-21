package com.ad_coding.noteappcourse.componentes

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.TimeUnit

class MyAlarmReceiver : BroadcastReceiver() {
    private var countDownTimer: CountDownTimer? = null

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        // Establecer el tiempo que queda para que suene la alarma (en milisegundos)
        val timeRemainingMillis = intent.getLongExtra(EXTRA_TIME_REMAINING, 0)

        // Iniciar el temporizador para actualizar el contador
        startCountdownTimer(context, timeRemainingMillis)

        // Crear la notificación
        createNotification(context)
    }

    private fun createNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio")
            .setContentText("Tu recordatorio para la fecha seleccionada.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Handle the permission request if needed
                return
            }
            notify(notificationId++, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Mi Canal de Notificaciones"
            val descriptionText = "Descripción de mi canal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startCountdownTimer(context: Context, timeRemainingMillis: Long) {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeRemainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Actualizar el contador cada segundo (1000 milisegundos)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                // Puedes enviar este tiempo a tu UI o almacenarlo para su uso
                val timeRemainingText = String.format("%02d:%02d", minutes, seconds)
                updateNotification(context, timeRemainingText)
            }

            override fun onFinish() {
                // Aquí puedes realizar acciones adicionales cuando el temporizador llega a cero
            }
        }

        countDownTimer?.start()
    }

    private fun updateNotification(context: Context, timeRemainingText: String) {
        // Puedes actualizar el contenido de la notificación con el tiempo restante
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio")
            .setContentText("Tu recordatorio en $timeRemainingText")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }

    companion object {
        const val CHANNEL_ID = "ALARM_CHANNEL"
        private var notificationId = 1

        // Clave extra para el tiempo restante en milisegundos
        const val EXTRA_TIME_REMAINING = "extra_time_remaining"
    }
}
