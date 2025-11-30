package com.example.gestaoabrigoanimais.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.gestaoabrigoanimais.data.model.Animal // Importação de Animal (CRÍTICO)
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object VaccineScheduler {

    private const val VACCINE_REMINDER_REQUEST_CODE = 900

    fun scheduleVaccineReminder(context: Context, animal: Animal) {
        val proximaVacinaData = animal.proximaVacinaData
        if (proximaVacinaData.isNullOrBlank()) {
            cancelVaccineReminder(context, animal.id)
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Trata o parse da data (resolve warnings e type mismatch)
        val date: java.util.Date = sdf.parse(proximaVacinaData) ?: return

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            return
        }

        val intent = Intent(context, VaccineReminderReceiver::class.java).apply {
            // As referências aqui dependem da importação correta da classe Animal
            putExtra("ANIMAL_NAME", animal.nome)
            putExtra("VACCINE_DATE", proximaVacinaData)
            data = android.net.Uri.parse("custom://animal/${animal.id}")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            animal.id + VACCINE_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelVaccineReminder(context: Context, animalId: Int) {
        val intent = Intent(context, VaccineReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            animalId + VACCINE_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}