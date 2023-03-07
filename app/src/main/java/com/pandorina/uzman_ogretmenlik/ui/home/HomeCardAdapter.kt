package com.pandorina.uzman_ogretmenlik.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.databinding.ItemHomeCardBinding
import com.pandorina.uzman_ogretmenlik.ui.test.TrialExamTestsFragment
import com.pandorina.uzman_ogretmenlik.ui.title.TitlesFragment
import me.grantland.widget.AutofitHelper

class HomeCardAdapter(
    val onClickRandomQuestions: () -> Unit,
    val navigate: (Fragment) -> Unit
): RecyclerView.Adapter<HomeCardAdapter.Holder>() {
    class Holder(
        private val binding: ItemHomeCardBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(
            title: String = "",
            description: String = "",
            icon: Int = 0,
            backgroundColor: Int = 0,
            onClick: () -> Unit = {}
        ){
            binding.apply {
                root.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))
                cardTitle.text = title
                AutofitHelper.create(cardDescription)
                cardDescription.text = description
                cardIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, icon))
                cardButton.setOnClickListener {
                    onClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHomeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.itemView.context
        when(position){
            0 -> {
                holder.bind(
                    title = context.getString(R.string.tests_by_topics_card_title),
                    description = context.getString(R.string.tests_by_topics_card_description),
                    icon = R.drawable.ic_topi,
                    backgroundColor = R.color.light_blue
                ){
                    navigate(TitlesFragment())
                }
            }
            1 -> {
                holder.bind(
                    title = context.getString(R.string.random_tests_card_title),
                    description = context.getString(R.string.random_tests_card_description),
                    icon = R.drawable.ic_random,
                    backgroundColor = R.color.dark_green
                ){
                    onClickRandomQuestions()
                }
            }
            2 -> {
                holder.bind(
                    title = context.getString(R.string.trial_exams_card_title),
                    description = context.getString(R.string.trial_exams_card_description),
                    icon = R.drawable.ic_trial_exam,
                    backgroundColor = R.color.black
                ){
                    navigate(TrialExamTestsFragment())
                }
            }
    }
    }

    override fun getItemCount(): Int = 3
}