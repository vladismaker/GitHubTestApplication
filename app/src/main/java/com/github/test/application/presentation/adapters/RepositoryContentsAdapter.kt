package com.github.test.application.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.test.application.R
import com.github.test.application.domain.dataclasses.DataRepositoryContentEntry

class RepositoryContentsAdapter(
    private val listContent: List<DataRepositoryContentEntry>,
    private val onRepoClick: (DataRepositoryContentEntry) -> Unit
) :
    RecyclerView.Adapter<RepositoryContentsAdapter.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val linearView: LinearLayout) : RecyclerView.ViewHolder(linearView)

    override fun getItemCount(): Int {
        return listContent.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.item_repository_content, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.linearView

        val name: TextView = itemView.findViewById(R.id.content_name_text_view)
        val image: ImageView = itemView.findViewById(R.id.image_file_or_folder)

        val itemFromList = listContent[position]
        val nameString = listContent[position].name
        val typeString = listContent[position].type
        name.text = nameString

        if (typeString == contextAdapter.getString(R.string.sign_folder)){
            image.setImageResource(R.drawable.icon_folder)
        }else{
            image.setImageResource(R.drawable.icon_file)
        }
        itemView.setOnClickListener{
            onRepoClick(itemFromList)
        }
    }
}