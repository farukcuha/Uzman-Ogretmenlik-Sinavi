package com.pandorina.uzman_ogretmenlik.util

import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.pandorina.uzman_ogretmenlik.UzmanOgretmenApplication

class AppUpdateManager {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(UzmanOgretmenApplication.get())

    companion object {
        const val UPDATE_REQUEST = 113
    }

    fun checkUpdateInfo(activity: Activity) {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfoTask
            .addOnSuccessListener { appUpdateInfo ->
                val updateAvailable =
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                val updateImmediate: Boolean =
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                val updateFlexible: Boolean =
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                val availableVersion: Int = appUpdateInfo.availableVersionCode()
                Log.d(
                    "update_info", "update available = " + updateAvailable +
                            " immediate = " + updateImmediate +
                            " flexible = " + updateFlexible +
                            " available version = " + availableVersion
                )
                if (updateAvailable && updateImmediate) {
                    startUpdate(activity, appUpdateInfo)
                }
            }
            .addOnFailureListener { e ->
                Log.d(
                    "update_info",
                    "check update info error: " + e.localizedMessage
                )
            }
    }

    private fun startUpdate(context: Activity, appUpdateInfo: AppUpdateInfo?) {
        if (appUpdateInfo == null) {
            return
        }
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                context,
                UPDATE_REQUEST
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }
}
