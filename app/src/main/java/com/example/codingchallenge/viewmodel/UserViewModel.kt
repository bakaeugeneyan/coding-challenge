package com.example.codingchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.codingchallenge.utils.Constants.Companion.QUERY_PAGE

class UserViewModel(application: Application): AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["numbers"] = QUERY_PAGE
        return queries
    }
}