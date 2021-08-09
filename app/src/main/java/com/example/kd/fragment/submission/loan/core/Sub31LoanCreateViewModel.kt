package com.example.kd.fragment.submission.loan.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sub31LoanCreateViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    fun login(username: String, token: String) {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {
        }
    }
}