package com.ad_coding.noteappcourse.componentes


import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.ad_coding.noteappcourse.ViewModel.EstadoFecha
import com.ad_coding.noteappcourse.ui.screen.note.NoteEvent
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DatePickerFecha(
    estadoFecha: EstadoFecha,
    onEvent: (NoteEvent) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var date by remember { mutableStateOf("${day}/${month + 1}/${year}") }

    Column {
        Button(onClick = {
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Guardar la fecha seleccionada temporalmente
                val fechaTemporal =
                    LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDayOfMonth, 0, 0)

                // Abrir TimePickerDialog después de seleccionar la fecha
                TimePickerDialog(context, { _, hourOfDay, minute ->
                    // Combinar fecha y hora
                    val fechaConHora = fechaTemporal.withHour(hourOfDay).withMinute(minute)

                    // Formatear fecha y hora
                    val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                    estadoFecha.estadoFecha = fechaConHora.format(formatoFecha)

                    // Actualizar la variable 'date' y programar la alarma
                    date = fechaConHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))


                }, fechaTemporal.hour, fechaTemporal.minute, true).show()

            }, year, month, day).show()

        }) {
            Text("Fecha")
        }

        Text(text = "Fecha: $date")
        onEvent(NoteEvent.FechaCambio(date))

    }

}

private fun scheduleAlarm(context: Context, year: Int, month: Int, day: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MyAlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val calendar = Calendar.getInstance()
    calendar.set(year, month, day, 8, 0) // Ajusta la hora y los minutos según tus necesidades

    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
}
