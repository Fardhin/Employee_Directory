package com.example.employeedirectory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.employeedirectory.R
import com.example.employeedirectory.model.User

class UserAdapter(private val context: Context, private val userList: List<User>) : BaseAdapter() {

    override fun getCount(): Int = userList.size

    override fun getItem(position: Int): Any = userList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val user = getItem(position) as User
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val avatarImage: ImageView = view.findViewById(R.id.avatarImage)


        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        tvUserId.text = "ID: ${user.id}"  // Set the user ID

        tvName.text = "${user.first_name} ${user.last_name}"
        tvEmail.text = user.email


        val avatarUrl = "https://reqres.in/img/faces/${user.id}-image.jpg"

        Glide.with(context)
            .load(avatarUrl)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .circleCrop()
            .into(avatarImage)

        return view
    }
}
