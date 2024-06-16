package com.github.test.application.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.test.application.R
import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.SearchResult
import com.github.test.application.presentation.viewholders.RepositoryViewHolder
import com.github.test.application.presentation.viewholders.UserViewHolder

class SearchResultAdapter(private val onRepoClick: (DataRepository) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<SearchResult>()

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_REPOSITORY = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<SearchResult>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SearchResult.UserResult -> VIEW_TYPE_USER
            is SearchResult.RepoResult -> VIEW_TYPE_REPOSITORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }
            VIEW_TYPE_REPOSITORY -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
                RepositoryViewHolder(view, onRepoClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SearchResult.UserResult -> (holder as UserViewHolder).bind(item.user)
            is SearchResult.RepoResult -> (holder as RepositoryViewHolder).bind(item.repo)
        }
    }

    override fun getItemCount(): Int = items.size
}