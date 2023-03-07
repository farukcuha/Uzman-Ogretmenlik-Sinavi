package com.pandorina.uzman_ogretmenlik.ui.test

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pandorina.uzman_ogretmenlik.data.local.StatisticEntity
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import com.pandorina.uzman_ogretmenlik.databinding.ItemExamTestBinding
import com.pandorina.uzman_ogretmenlik.util.getEmojiAnimationRes
import com.pandorina.uzman_ogretmenlik.util.getSuccessPercentage

class ExamTestsAdapter(
    private val tests: List<TestWithResult>,
    private val onClick: (String, Int) -> Unit = { _, _ -> }
): RecyclerView.Adapter<ExamTestsAdapter.Holder>() {

    data class TestWithResult(
        val test: TestsResponse.Test,
        val result: StatisticEntity?
    )

    class Holder(private val binding: ItemExamTestBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(testWithResult: TestWithResult, onClick: (String, Int) -> Unit){
            val test = testWithResult.test
            binding.tvTestTitle.text = "${test.testTitle} - ${test.testNo}"
            binding.root.setOnClickListener {
                onClick(test.testTitle ?: return@setOnClickListener, test.testNo ?: 0 )
            }
            testWithResult.result?.let { result ->
                binding.resultContainer.isVisible = true
                binding.tvCorrectCount.text = "${result.correctCount}"
                binding.tvWrongCount.text = "${result.wrongCount}"
                val successPercentage = getSuccessPercentage(result.correctCount.toFloat(), result.wrongCount.toFloat())
                binding.tvPercentage.text = "% $successPercentage"
                binding.reactionAnimation.setAnimation(successPercentage.getEmojiAnimationRes())
            } ?: kotlin.run {
                binding.resultContainer.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemExamTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(tests[position], onClick)
    }

    override fun getItemCount(): Int = tests.size
}