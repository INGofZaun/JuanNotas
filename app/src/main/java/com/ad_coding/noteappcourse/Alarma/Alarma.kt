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

class Alarma: BroadcastReceiver(){



    override fun onReceive(context: Context?, intent: Intent?) {

        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Revisa tus tareas pendientes...")
                .setContentText("Tienes tareas pendietes  $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
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
) : AlarmScheduler{

    //private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as  AlarmManager

    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, Alarma::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        //convrtir la fecha  y hora  auna fecha y hora  en la zona  horaria del sistema
        val zonedDateTime = ZonedDateTime.of(alarmItem.alarmTime, ZoneId.systemDefault())
        //convierte esa fech y hora  a milisegundos   y finalmete esos se ututiliza para programar la alarma
        val millis = zonedDateTime.toInstant().toEpochMilli()

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            millis-86400000,
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.e("Alarm", "Alarm set at ")
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, Alarma::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}


