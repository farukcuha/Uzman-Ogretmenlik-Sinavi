package com.pandorina.uzman_ogretmenlik.ui.test

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentExamTestsBinding
import com.pandorina.uzman_ogretmenlik.ui.question.QuestionsFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrialExamTestsFragment : BaseFragment<FragmentExamTestsBinding>(
    inflateMethod = FragmentExamTestsBinding::inflate,
    homeButtonEnabled = true,
    showActionBar = true
){

    private val viewModel: TrialExamTestsViewModel by viewModels()

    override fun onViewCreated() {
        setActionBarTitle(getString(R.string.trial_exams_card_title))

        viewModel.fetchExamTests()

        viewModel.testResults.observe(viewLifecycleOwner){
            binding.rvExamTests.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ExamTestsAdapter(it){ title, testNo ->
                    viewModel.testNo = testNo
                    viewModel.fetchQuestions()
                }
            }
        }

        viewModel.questions.observe(viewLifecycleOwner){ questions ->
            if (questions.isNotEmpty()){
                navigate(
                    QuestionsFragment.getInstance(
                        title = "${getString(R.string.trial_exams_card_title)} - ${viewModel.testNo}",
                        questions = questions
                    )
                )
                viewModel.clearQuestions()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.pbExamTests.isVisible = isLoading
        }
    }
}