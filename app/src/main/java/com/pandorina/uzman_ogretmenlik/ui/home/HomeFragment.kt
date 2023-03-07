package com.pandorina.uzman_ogretmenlik.ui.home

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentHomeBinding
import com.pandorina.uzman_ogretmenlik.util.getSuccessPercentage
import com.pandorina.uzman_ogretmenlik.ui.MainActivity
import com.pandorina.uzman_ogretmenlik.ui.SettingsFragment
import com.pandorina.uzman_ogretmenlik.ui.question.QuestionsFragment
import com.pandorina.uzman_ogretmenlik.util.LottieDialogFragment
import com.pandorina.uzman_ogretmenlik.util.NetworkConnection
import com.pandorina.uzman_ogretmenlik.util.in_app_review.GNTReviewManager
import com.pandorina.uzman_ogretmenlik.util.initViewIntent
import dagger.hilt.android.AndroidEntryPoint
import me.grantland.widget.AutofitHelper
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    inflateMethod = FragmentHomeBinding::inflate,
    homeButtonEnabled = false,
    showActionBar = true
) {

    private val viewModel: HomeViewModel by viewModels()
    var findingRandomTestsDialog: LottieDialogFragment? = null
    private val dateYearFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    private var runnable: Runnable? = null
    var handler: Handler = Handler(Looper.getMainLooper())


    override fun onViewCreated() {
        setActionBarTitle(getString(R.string.home))
        viewModel.observe()
        initCardPager()

        AutofitHelper.create(binding.examCountdown)
        AutofitHelper.create(binding.tvCorrectCount)
        AutofitHelper.create(binding.tvWrongCount)
        AutofitHelper.create(binding.tvSuccessPercentage)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        navigate(SettingsFragment())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        GNTReviewManager.with(requireActivity())
            .setInstallDays(2)
            .setLaunchTimes(3)
            .setRemindInterval(2)
            .setDebug(false)
            .monitor()

        GNTReviewManager.showRateDialogIfMeetsConditions(requireActivity())
    }

    private fun startAutoSlider() {
        runnable = Runnable {
            if(_binding == null) return@Runnable
            var pos = binding.cardsPager.currentItem
            pos ++
            if (pos >= 3) pos = 0
            binding.cardsPager.setCurrentItem(pos, true)
            runnable?.let { handler.postDelayed(it, 4000) }
        }
        runnable?.let { handler.postDelayed(it, 4000) }
    }

    override fun onDestroy() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onDestroy()
    }

    override fun onPause() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    private fun HomeViewModel.observe(){
        examTime.observe(viewLifecycleOwner){
            if (it != null) {
                initCountdown(it)
                setExactDate(it)
            }
        }

        randomQuestions.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                findingRandomTestsDialog?.dismiss()
                navigate(
                    QuestionsFragment.getInstance(
                        title = getString(R.string.random_tests_card_title),
                        questions = it
                    )
                )
                viewModel.clearRandomQuestions()
            }
        }

        correctCount.observe(viewLifecycleOwner){
            setStatisticData()
        }

        wrongCount.observe(viewLifecycleOwner){
            setStatisticData()
        }
    }

    @SuppressLint("SetTextI18n")
    fun setStatisticData(){
        val correctCount = viewModel.correctCount.value ?: 0
        val wrongCount = viewModel.wrongCount.value ?: 0
        binding.tvCorrectCount.text = "$correctCount"
        binding.tvWrongCount.text = "$wrongCount"
        binding.tvSuccessPercentage.text =
            "% ${getSuccessPercentage(correctCount.toFloat(), wrongCount.toFloat())}"
    }

    @SuppressLint("SetTextI18n")
    private fun setExactDate(time: Long){
        Date(time).apply {
            binding.examExactDate.text = getString(R.string.exam_exact_date, dateYearFormat.format(this))
        }
    }

    private fun initCountdown(time: Long) {
        val different = time - System.currentTimeMillis()
        object : CountDownTimer(different, 1000) {

            @SuppressLint("SetTextI18n", "StringFormatMatches")
            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24
                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli
                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli
                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli
                val elapsedSeconds = diff / secondsInMilli
                if (_binding == null) return

                binding.examCountdown.text = getString(R.string.countdown_text, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
            }

            override fun onFinish() {
                binding.examCountdown.text = getString(R.string.good_luck)
            }
        }.start()
    }

    private fun initCardPager(){
        val questionCount = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString(SettingsFragment.PREFERENCE_RANDOM_TEST_QUESTION_COUNT, "10")
        binding.cardsPager.adapter = HomeCardAdapter(onClickRandomQuestions = {
            if (!NetworkConnection.isConnected) {
                (activity as MainActivity).connectionErrorDialog?.show(childFragmentManager, null)
                return@HomeCardAdapter
            }
            viewModel.fetchRandomQuestions(questionCount?.toInt() ?: 10)
            findingRandomTestsDialog = LottieDialogFragment(
                lottieRes = R.raw.lottie_loading_random,
                message = getString(R.string.preparing_questions),
                cancellable = false
            )
            findingRandomTestsDialog?.show(childFragmentManager, null)
        }) {
            navigate(it)
        }
        binding.dotsIndicator.attachTo(binding.cardsPager)
        startAutoSlider()
    }
}