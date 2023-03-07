package com.pandorina.uzman_ogretmenlik.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.pandorina.uzman_ogretmenlik.BuildConfig
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.data.remote.UzmanOgretmenExamService
import com.pandorina.uzman_ogretmenlik.ui.home.HomeFragment
import com.pandorina.uzman_ogretmenlik.util.AppUpdateManager
import com.pandorina.uzman_ogretmenlik.util.InterstitialAdManager
import com.pandorina.uzman_ogretmenlik.util.LottieDialogFragment
import com.pandorina.uzman_ogretmenlik.util.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var connectionErrorDialog: LottieDialogFragment? = null
    var isInternetConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()

        InterstitialAdManager.initialize(this)
        AppUpdateManager().checkUpdateInfo(this)

        connectionErrorDialog = LottieDialogFragment(
            lottieRes = R.raw.lottie_error,
            "İnternet bağlantınızı kontrol ediniz."
        )
        NetworkConnection(this).observe(this){
            isInternetConnected = it
            if (!it) { connectionErrorDialog?.show(supportFragmentManager, null) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
}