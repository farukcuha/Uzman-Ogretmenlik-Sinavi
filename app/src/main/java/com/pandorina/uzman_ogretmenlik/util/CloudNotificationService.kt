package com.pandorina.uzman_ogretmenlik.util

import android.annotation.SuppressLint
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pandorina.uzman_ogretmenlik.ui.SettingsFragment
import com.pandorina.uzman_ogretmenlik.util.Notification.notifyPrices

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class CloudNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val text = message.notification?.body
        val notificationsEnabled = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getBoolean(SettingsFragment.PREFERENCE_NOTIFICATIONS_ENABLED, true)
        if (notificationsEnabled) notifyPrices(this, "cloud_message", title, text)
        super.onMessageReceived(message)
    }
}