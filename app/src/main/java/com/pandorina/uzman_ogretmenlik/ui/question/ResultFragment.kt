package com.pandorina.uzman_ogretmenlik.ui.question

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentResultBinding
import com.pandorina.uzman_ogretmenlik.util.InterstitialAdManager
import com.pandorina.uzman_ogretmenlik.util.getEmojiAnimationRes
import com.pandorina.uzman_ogretmenlik.util.getSuccessPercentage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment(
    private val title: String,
    private val correctCount: Float,
    private val wrongCount: Float,
    private val navigateBack: () -> Unit,
    private val navigateHome: () -> Unit
): BaseFragment<FragmentResultBinding>(
    inflateMethod = FragmentResultBinding::inflate,
    homeButtonEnabled = false,
    showActionBar = false
) {
    private var successPercentage = getSuccessPercentage(correctCount, wrongCount)
    val viewModel: ResultViewModel by viewModels()

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun onViewCreated() {
        binding.reactionAnimation.setAnimation(successPercentage.getEmojiAnimationRes())
        binding.reactionAnimation.playAnimation()
        binding.tvCorrectResultCount.text = getString(R.string.correct_count, correctCount.toInt())
        binding.tvWrongResultCount.text = getString(R.string.wrong_count, wrongCount.toInt())
        binding.tvSuccessPercentage.text = getString(R.string.success_percentage, successPercentage)

        viewModel.insertExamResult(
            if (title == getString(R.string.random_tests_card_title)) "${getString(R.string.random_tests_card_title)} - ${System.currentTimeMillis()}"
            else title,
            correctCount.toInt(),
            wrongCount.toInt())

        binding.btnContinue.setOnClickListener {
            navigateBack()
        }
        binding.btnBackToHome.setOnClickListener {
            navigateHome()
        }

        InterstitialAdManager.show()
    }
}