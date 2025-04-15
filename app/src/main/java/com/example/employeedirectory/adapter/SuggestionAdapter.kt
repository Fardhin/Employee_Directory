package com.example.employeedirectory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.employeedirectory.R
import com.example.employeedirectory.model.User

class SuggestionAdapter(
    private val context: Context,
    private var userList: List<User>
) : BaseAdapter() {

    fun updateList(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    override fun getCount(): Int = userList.size

    override fun getItem(position: Int): Any = userList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.suggestion_item, parent, false)

        val user = userList[position]


        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val avatarImageView = view.findViewById<ImageView>(R.id.avatarImageView)

        tvName.text = "${user.first_name} ${user.last_name}"
        tvEmail.text = user.email
        Glide.with(context)
            .load(user.avatar)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .into(avatarImageView)

        return view
    }

}
