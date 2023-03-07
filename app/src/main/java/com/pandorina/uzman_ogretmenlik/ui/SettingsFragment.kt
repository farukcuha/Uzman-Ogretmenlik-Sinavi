package com.pandorina.uzman_ogretmenlik.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.pandorina.uzman_ogretmenlik.*
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import com.pandorina.uzman_ogretmenlik.util.initMailIntent
import com.pandorina.uzman_ogretmenlik.util.initShareIntent
import com.pandorina.uzman_ogretmenlik.util.initViewIntent
import com.pandorina.uzman_ogretmenlik.util.showConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var statisticRepository: StatisticRepository

    companion object {
        const val PREFERENCE_RANDOM_TEST_QUESTION_COUNT = "random_test_question_count"
        const val PREFERENCE_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val KEY_CLEAR_RESULTS = "key_clear_results"

        const val KEY_SHARE_APP = "key_share_the_app"
        const val KEY_RATE_THE_APP = "key_rate_the_app"
        const val KEY_CONTACT = "key_contact"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Ayarlar"
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        findPreference<Preference>(KEY_CLEAR_RESULTS)?.setOnPreferenceClickListener {
            showConfirmationDialog(
                "Sonuçları Temizle",
                "Bütün testlerde yaptığın kayıtlı sonuçlarınız silinir. Bu işlem geri alınamaz",
                "Temzile"
            ){
                lifecycleScope.launch {
                    statisticRepository.clearExamResults()
                }
            }
            true
        }
        findPreference<Preference>(KEY_SHARE_APP)?.setOnPreferenceClickListener {
            requireContext().initShareIntent("Uygulamayı Paylaş!", "https://play.google.com/store/apps/details?id=com.pandorina.uzman_ogretmenlik")
            true
        }
        findPreference<Preference>(KEY_RATE_THE_APP)?.setOnPreferenceClickListener {
            requireContext().initViewIntent("https://play.google.com/store/apps/details?id=com.pandorina.uzman_ogretmenlik")
            true
        }
        findPreference<Preference>(KEY_CONTACT)?.setOnPreferenceClickListener {
            requireContext().initMailIntent()
            true
        }
    }
}