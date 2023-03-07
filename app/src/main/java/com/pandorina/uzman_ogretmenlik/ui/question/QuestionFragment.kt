package com.pandorina.uzman_ogretmenlik.ui.question

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentQuestionBinding
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.util.toJson
import com.pandorina.uzman_ogretmenlik.util.toObject

class QuestionFragment: BaseFragment<FragmentQuestionBinding>(
    inflateMethod = FragmentQuestionBinding::inflate,
    homeButtonEnabled = true,
    showActionBar = false
) {

    private var completion: (Int, Boolean) -> Unit = { _, _, -> }

    fun setCompletion(completion: (Int, Boolean) -> Unit){
        this.completion = completion
    }

    companion object {
        const val ARG_QUESTION = "arg_question"

        fun getInstance(question: QuestionsResponse.Question): QuestionFragment {
            return QuestionFragment().apply {
                arguments = bundleOf().apply {
                    putString(ARG_QUESTION, question.toJson<QuestionsResponse.Question>())
                }
            }
        }
    }

    data class Answer(
        val checkedPosition: Int?,
        val isCorrect: Boolean?
    )

    var answer: Answer? = null

    override fun onPause() {
        super.onPause()
        binding.adView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    override fun onViewCreated() {
        getArgument<String>(ARG_QUESTION){ argQuestion ->
            val question = argQuestion.toObject<QuestionsResponse.Question>()

            binding.adView.loadAd(AdRequest.Builder().build())
            binding.questionViewer.loadHtml(question.questionText)
            binding.rvAnswers.apply {
                adapter = AnswersAdapter(
                    question.answers ?: emptyList(),
                    question.correctAnswer ?: -1
                ){ checkedPosition, isCorrect ->
                    answer = Answer(checkedPosition, isCorrect)
                    completion(checkedPosition, isCorrect)
                }
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}