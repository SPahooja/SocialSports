package com.uwcs446.socialsports.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment with user: [$user]"
    }
    val text: LiveData<String> = _text

    var user = "NO-USER" // TODO: Fetch from user service
}
