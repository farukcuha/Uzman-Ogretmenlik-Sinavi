package com.pandorina.uzman_ogretmenlik.ui.test

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentExamTestsBinding
import com.pandorina.uzman_ogretmenlik.ui.question.QuestionsFragment
import com.pandorina.uzman_ogretmenlik.util.InterstitialAdManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExamTestsFragment: BaseFragment<FragmentExamTestsBinding>(
    inflateMethod = FragmentExamTestsBinding::inflate,
    homeButtonEnabled = true,
    showActionBar = true
) {
    private val viewModel: ExamTestsViewModel by viewModels()

    companion object {
        private const val ARG_TITLE = "arg_title"

        fun getInstance(title: String): ExamTestsFragment {
            return ExamTestsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
        }
    }

    override fun onViewCreated() {
        arguments?.getString(ARG_TITLE)?.let {
            setActionBarTitle(it)
            viewModel.title = it
            viewModel.fetchExamTests()

            viewModel.questions.observe(viewLifecycleOwner){ questions ->
                if (questions.isNotEmpty()){
                    navigate(
                        QuestionsFragment.getInstance(
                            title = "${viewModel.title} - ${viewModel.testNo}",
                            questions = questions
                        )
                    )
                    viewModel.clearQuestions()
                }
            }

            viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
                binding.pbExamTests.isVisible = isLoading
            }

            viewModel.testResults.observe(viewLifecycleOwner){
                binding.rvExamTests.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ExamTestsAdapter(it){ title, testNo ->
                        viewModel.title = title
                        viewModel.testNo = testNo
                        viewModel.fetchQuestions()
                    }
                }
            }
        }
    }
}