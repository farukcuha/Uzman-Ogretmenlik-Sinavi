package com.pandorina.uzman_ogretmenlik.ui.title

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.core.BaseFragment
import com.pandorina.uzman_ogretmenlik.databinding.FragmentTitlesBinding
import com.pandorina.uzman_ogretmenlik.ui.test.ExamTestsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TitlesFragment : BaseFragment<FragmentTitlesBinding>(
    inflateMethod = FragmentTitlesBinding::inflate,
    homeButtonEnabled = true,
    showActionBar = true
) {

    private val viewModel: ExamTitlesViewModel by viewModels()

    override fun onViewCreated() {
        setActionBarTitle(getString(R.string.topics))

        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.pbExamTitles.isVisible = it
        }

        viewModel.titles.observe(viewLifecycleOwner){
            binding.rvExamTitles.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = ExamTitleAdapter(it ?: emptyList()){
                    navigate(ExamTestsFragment.getInstance(it))
                }
            }
        }
    }
}