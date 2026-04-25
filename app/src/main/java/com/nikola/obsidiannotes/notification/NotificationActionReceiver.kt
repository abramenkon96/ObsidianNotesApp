package com.nikola.obsidiannotes.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val fileName = intent.getStringExtra("note_file_name") ?: return
        val action = intent.action

        if (action == "UNPIN_NOTE") {
            val helper = NotificationHelper(context)
            helper.cancelNotification(fileName)
            
            // В реальном приложении здесь также нужно обновить YAML в файле (pinned: false)
            // Это можно сделать через WorkManager или прямо в сервисе
        }
    }
}
