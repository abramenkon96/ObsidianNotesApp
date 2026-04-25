package com.nikola.obsidiannotes.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nikola.obsidiannotes.MainActivity
import com.nikola.obsidiannotes.R
import com.nikola.obsidiannotes.model.Note

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "pinned_notes_channel"
        const val CHANNEL_NAME = "Закрепленные заметки"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Уведомления для закрепленных заметок"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(note: Note): Notification {
        // Клик по самому уведомлению открывает приложение
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("note_file_name", note.fileName)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            note.fileName.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Кнопка "Открепить" (здесь будет Intent для сервиса)
        val unpinIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "UNPIN_NOTE"
            putExtra("note_file_name", note.fileName)
        }
        val unpinPendingIntent = PendingIntent.getBroadcast(
            context,
            note.fileName.hashCode(),
            unpinIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(note.title)
            .setContentText(note.content.take(100)) // Показываем начало текста
            .setSmallIcon(android.R.drawable.ic_menu_edit) // Можно заменить на свою иконку
            .setOngoing(true) // Делает уведомление неудаляемым свайпом
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Открепить", unpinPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun showNotification(note: Note) {
        notificationManager.notify(note.fileName.hashCode(), buildNotification(note))
    }

    fun cancelNotification(fileName: String) {
        notificationManager.cancel(fileName.hashCode())
    }
}
