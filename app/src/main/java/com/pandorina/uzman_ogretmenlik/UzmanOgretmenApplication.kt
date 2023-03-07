package com.pandorina.uzman_ogretmenlik

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import dagger.hilt.android.HiltAndroidApp
import papaya.`in`.admobopenads.AppOpenManager

@HiltAndroidApp
class UzmanOgretmenApplication : Application() {

    companion object {
        lateinit var app: UzmanOgretmenApplication

        fun get(): UzmanOgretmenApplication {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this){}
        AppOpenManager(this, BuildConfig.APP_OPEN_AD_UNIT_ID)
        app = this
    }
}
