package com.github.test.application.presentation.viewholders

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.test.application.R
import com.github.test.application.domain.dataclasses.DataUser
import com.squareup.picasso.Picasso

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
    private val loginTextView: TextView = itemView.findViewById(R.id.loginTextView)
    private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)

    fun bind(user: DataUser) {
        loginTextView.text = user.login
        scoreTextView.text = user.score
        Picasso.get()
            .load(user.avatar_url)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .into(avatarImageView)

        itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.html_url))
            itemView.context.startActivity(intent)
        }
    }
}