package com.ad_coding.noteappcourse.Alarma

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ad_coding.noteappcourse.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Alarma: BroadcastReceiver(){



    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Alarma", "onReceive triggered") // Añadir un log para verificar si se llama al método
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Revisa tus tareas pendientes...")
                .setContentText("Tienes tareas pendientes $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Esto asegura que la notificación tenga sonido, vibración, etc.
            notificationManager.notify(1, builder.build())
            Log.e("Alarma", "Notification sent") // Log para verificar que la notificación fue enviada
        }
    }

}

data class AlarmItem(
    val alarmTime : LocalDateTime,
    val message : String
)

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, Alarma::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        // Usar la fecha y hora del alarmItem
        val zonedDateTime = alarmItem.alarmTime.atZone(ZoneId.systemDefault()) // Zona horaria correcta
        val millis = zonedDateTime.toInstant().toEpochMilli() // Millisegundos desde el epoch

        // Crear el PendingIntent para el BroadcastReceiver
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Programar la alarma
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            millis, // Usa la hora exacta seleccionada
            pendingIntent
        )

        // Log de la hora programada (en formato legible)
        val formattedTime = alarmItem.alarmTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        Log.e("Alarm", "Alarm set for: $millis at $formattedTime")
    }

    override fun cancel(alarmItem: AlarmItem) {
        val intent = Intent(context, Alarma::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}




