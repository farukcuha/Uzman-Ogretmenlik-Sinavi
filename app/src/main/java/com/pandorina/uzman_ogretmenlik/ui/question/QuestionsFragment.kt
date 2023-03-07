package com.pandorina.uzman_ogretmenlik.ui.question

import android.annotation.SuppressLint
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentQuestionsBinding
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.util.showConfirmationDialog
import com.pandorina.uzman_ogretmenlik.util.InterstitialAdManager
import com.pandorina.uzman_ogretmenlik.util.toJson
import com.pandorina.uzman_ogretmenlik.util.toObject
import dagger.hilt.android.AndroidEntryPoint
import me.grantland.widget.AutofitHelper

@AndroidEntryPoint
class QuestionsFragment: BaseFragment<FragmentQuestionsBinding>(
    inflateMethod = FragmentQuestionsBinding::inflate,
    homeButtonEnabled = true,
    showActionBar = false
) {
    private val questionFragments = mutableListOf<QuestionFragment>()
    var questions: List<QuestionsResponse.Question> = emptyList()
    var title: String = ""

    private var correctCount = MutableLiveData(0)
    var wrongCount = MutableLiveData(0)

    companion object {
        const val ARG_TITLE = "arg_title"
        const val ARG_QUESTION_LIST = "arg_question_list"

        fun getInstance(title: String, questions: List<QuestionsResponse.Question>): QuestionsFragment {
            return QuestionsFragment().apply {
                arguments = bundleOf().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_QUESTION_LIST, questions.toJson<List<QuestionsResponse.Question>>())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated() {
        getArgument<String>(ARG_TITLE){
            title = it
            binding.tvTitle.text = it
        }
        getArgument<String>(ARG_QUESTION_LIST){
            questions = it.toObject<List<QuestionsResponse.Question>>()

            initViewPager()
            AutofitHelper.create(binding.tvTitle)
            binding.tvQuestionCount.text = questions.size.toString()
            binding.btnClose.setOnClickListener {
                if (getTestState()) {
                    parentFragmentManager.popBackStack()
                    return@setOnClickListener
                }
                showConfirmationDialog(
                    getString(R.string.cancel_the_test),
                    getString(R.string.cancel_the_test_confirmation),
                    getString(R.string.yes)
                ){
                    parentFragmentManager.popBackStack()
                }
            }
            InterstitialAdManager.show()
        }
    }



    @SuppressLint("SetTextI18n")
    private fun initViewPager(){
        questionFragments.clear()
        questions.forEach { question ->
            val questionFragmentInstance = QuestionFragment.getInstance(question)
            questionFragmentInstance.setCompletion{ _, _ ->
                binding.btnRightArrow.isVisible = true
                getTestState()
            }
            questionFragments.add(questionFragmentInstance)

        }
        binding.viewPager2.adapter = QuestionsFragmentAdapter(requireActivity(),
            questionFragments.toList())
        binding.dotsIndicator.attachTo(binding.viewPager2)

        correctCount.observe(viewLifecycleOwner){
            binding.tvCorrectCount.text = it.toString()
        }

        wrongCount.observe(viewLifecycleOwner){
            binding.tvWrongCount.text = it.toString()
        }

        binding.btnRightArrow.setOnClickListener {
            if (getTestState()) navigateToResults()
            else toNextQuestion()
        }
        binding.viewPager2.isUserInputEnabled = false

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvQuestionNumber.text = "${position + 1}"
                if (questionFragments[position].answer?.isCorrect != null){
                    binding.btnRightArrow.isVisible = true
                    binding.btnRightArrow.setAnimation(R.raw.lottie_next)
                    binding.btnRightArrow.playAnimation()
                }
                else {
                    binding.btnRightArrow.isVisible = false
                    binding.btnRightArrow.playAnimation()
                }
            }
        })
    }



    private fun getTestState(): Boolean {
        correctCount.value = 0
        wrongCount.value = 0
        questionFragments.forEach {
            if (it.answer?.isCorrect == true) correctCount.value = correctCount.value!! + 1
            else if (it.answer?.isCorrect == false) wrongCount.value = wrongCount.value!! + 1
        }
        return correctCount.value!! + wrongCount.value!! == questionFragments.size
    }

    private fun navigateToResults(){
        childFragmentManager
            .beginTransaction()
            .replace(R.id.viewPagerContainer,
                ResultFragment(
                    title,
                    correctCount.value!!.toFloat(),
                    wrongCount.value!!.toFloat(),
                    navigateBack = {
                        parentFragmentManager.popBackStack()
                    },
                    navigateHome = {
                        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                ))
            .commit()
        binding.btnRightArrow.isVisible = false
        binding.dotsIndicator.visibility = View.INVISIBLE
        binding.tvQuestionNumber.isVisible = false
        binding.tvQuestionCount.isVisible = false
        binding.tvResults.isVisible = true
    }

    private fun toNextQuestion(){
        binding.viewPager2.setCurrentItem(binding.viewPager2.currentItem + 1, true)
    }
}