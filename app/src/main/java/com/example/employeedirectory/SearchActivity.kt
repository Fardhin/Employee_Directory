package com.example.employeedirectory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.employeedirectory.adapter.SuggestionAdapter
import com.example.employeedirectory.model.User
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchInputLayout: TextInputLayout
    private lateinit var suggestionList: ListView
    private lateinit var userList: List<User>
    private lateinit var adapter: SuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchInput = findViewById(R.id.searchInput)
        searchInputLayout = findViewById(R.id.searchInputLayout)
        suggestionList = findViewById(R.id.suggestionList)

        userList = intent.getParcelableArrayListExtra("userList") ?: emptyList()
        adapter = SuggestionAdapter(this, emptyList()) // Start with no suggestions
        suggestionList.adapter = adapter


        searchInputLayout.setStartIconOnClickListener {
            finish()
        }

        searchInput.requestFocus()
        searchInput.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)


        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isEmpty()) {
                    adapter.updateList(emptyList())
                } else {
                    val filtered = userList.filter {
                        it.first_name.contains(query, ignoreCase = true) ||
                                it.last_name.contains(query, ignoreCase = true) ||
                                it.email.contains(query, ignoreCase = true)
                    }
                    adapter.updateList(filtered)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
