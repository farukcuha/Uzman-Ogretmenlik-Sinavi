package com.pandorina.uzman_ogretmenlik.ui.question

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse

class QuestionsFragmentAdapter(
    activity: FragmentActivity,
    private val questionFragments: List<QuestionFragment>
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = questionFragments.size

    override fun createFragment(position: Int): Fragment {
        return questionFragments[position]
    }
}