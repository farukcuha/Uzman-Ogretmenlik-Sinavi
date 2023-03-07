package com.pandorina.uzman_ogretmenlik.ui.title

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandorina.uzman_ogretmenlik.databinding.ItemExamTitleBinding

class ExamTitleAdapter(
    private val titles: List<String>,
    private val onClick: (String) -> Unit = {}
): RecyclerView.Adapter<ExamTitleAdapter.Holder>() {

    class Holder(private val binding: ItemExamTitleBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(title: String, position: Int, onClick: (String) -> Unit){
            binding.topicTitle.text = "${(position + 1)} - $title"
            binding.root.setOnClickListener {
                onClick(title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemExamTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(titles[position], position, onClick = onClick)
    }

    override fun getItemCount(): Int = titles.size
}