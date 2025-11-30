package com.example.gestaoabrigoanimais.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class VaccineReminderReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "vaccine_reminder_channel"
    private val NOTIFICATION_ID = 101

    override fun onReceive(context: Context, intent: Intent) {
        val animalName = intent.getStringExtra("ANIMAL_NAME") ?: "um animal"
        val vaccineDate = intent.getStringExtra("VACCINE_DATE") ?: "hoje"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Lembrete de Vacina")
            .setContentText("Atenção: A vacina de $animalName está agendada para $vaccineDate.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}