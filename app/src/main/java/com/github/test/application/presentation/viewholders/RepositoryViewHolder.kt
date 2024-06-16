package com.github.test.application.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.test.application.R
import com.github.test.application.domain.dataclasses.DataRepository

class RepositoryViewHolder(itemView: View, private val onRepoClick: (DataRepository) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val repoNameTextView: TextView = itemView.findViewById(R.id.repoNameTextView)
    private val repoDescriptionTextView: TextView = itemView.findViewById(R.id.repoDescriptionTextView)
    private val repoForksCountTextView: TextView = itemView.findViewById(R.id.repoForksCountTextView)

    fun bind(repository: DataRepository) {
        repoNameTextView.text = repository.name
        repoDescriptionTextView.text = repository.description
        val forksCount = "${repository.forks_count}\nForks"
        repoForksCountTextView.text = forksCount

        itemView.setOnClickListener {
            onRepoClick(repository)
        }
    }
}