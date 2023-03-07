package com.pandorina.uzman_ogretmenlik.ui.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.databinding.ItemQuestionAnswerBinding

class AnswersAdapter(
    private val answers: List<String?>,
    private val correctAnswer: Int,
    val onClick: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<AnswersAdapter.Holder>() {

    var checkedAnswer = -1

    inner class Holder(
        val binding: ItemQuestionAnswerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(answer: String) {
            binding.tvAnswer.text = answer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemQuestionAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.binding.root.context
        holder.bind(answers[position] ?: "")
        holder.binding.root.setOnClickListener {
            if (checkedAnswer != -1) return@setOnClickListener
            checkedAnswer = position
            onClick(checkedAnswer, checkedAnswer == correctAnswer)
            if (correctAnswer == checkedAnswer){
                holder.binding.answerAnimation.apply {
                    isVisible = true
                    holder.binding.root.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.correct_bg_color
                        )
                    )
                    setAnimation(R.raw.lottie_correct)
                    playAnimation()
                }
            } else {
                holder.binding.answerAnimation.apply {
                    isVisible = true
                    holder.binding.root.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wrong_bg_color
                        )
                    )
                    setAnimation(R.raw.lottie_wrong)
                    playAnimation()
                }
            }
            holder.binding.radioButton.isChecked = true
            if (position != correctAnswer) notifyItemChanged(correctAnswer)
        }

        if (checkedAnswer != -1){
            holder.binding.answerAnimation.apply {
                isVisible = true
                setAnimation(R.raw.lottie_correct)
                playAnimation()
            }
        }
    }

    override fun getItemCount(): Int = answers.size
}