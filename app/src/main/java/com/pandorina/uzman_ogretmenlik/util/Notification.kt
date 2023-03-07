package com.pandorina.uzman_ogretmenlik.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.ui.MainActivity

object Notification {
    private const val NOTIFICATION_CHANNEL_ID = "UZMAN_OGRETMEN_NOTIFICATION"

    @SuppressLint("UnspecifiedImmutableFlag")
    fun notifyPrices(
        context: Context,
        name: String,
        title: String?,
        text: String?,
    ) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val flag = if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE
                    else PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, flag
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                importance
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL_ID
        ).apply {
            setContentTitle(title)
            setContentText(text)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            priority = NotificationCompat.PRIORITY_HIGH
            setSmallIcon(R.mipmap.ic_launcher)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setWhen(System.currentTimeMillis())
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            setChannelId(NOTIFICATION_CHANNEL_ID)
            setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }
        val notification = builder.build()
        notificationManager.notify(101, notification)
    }
}