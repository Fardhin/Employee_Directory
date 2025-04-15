package com.example.employeedirectory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeedirectory.adapter.UserAdapter
import com.example.employeedirectory.model.User
import com.example.employeedirectory.model.UserResponse
import com.example.employeedirectory.network.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var etSearch: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private var userList = listOf<User>()
    private lateinit var totalCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        totalCountTextView = findViewById(R.id.totalCountTextView)

        listView = findViewById(R.id.listView)
        etSearch = findViewById(R.id.etSearch)
        progressBar = findViewById(R.id.progressBar)


        etSearch.isFocusable = false
        etSearch.isCursorVisible = false

        // Open SearchActivity on click
        etSearch.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            intent.putParcelableArrayListExtra("userList", ArrayList(userList))
            startActivity(intent)
        }

        // Listener to remove cursor when user taps on a user item
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            etSearch.clearFocus()
            etSearch.isCursorVisible = false
        }

        fetchUsers()

        // Optional: keeping this here for in-place search if needed
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val filteredList = userList.filter {
                    it.first_name.contains(query, ignoreCase = true)
                }
                updateListView(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()
        // Reset search bar state on return
        etSearch.clearFocus()
        etSearch.isFocusable = false
        etSearch.isCursorVisible = false
    }

    private fun fetchUsers() {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getUsers().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    userList = response.body()?.data ?: emptyList()
                    updateListView(userList)
                    totalCountTextView.text = "Total: ${userList.size} people"

                } else {
                    Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateListView(users: List<User>) {
        val adapter = UserAdapter(this, users)
        listView.adapter = adapter
        val totalText = if (users.isEmpty()) "No results found" else "Total: ${users.size} people"
        totalCountTextView.text = "Total: ${users.size} people"

    }
}
