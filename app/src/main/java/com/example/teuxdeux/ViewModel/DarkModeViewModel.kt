package com.example.teuxdeux.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DarkModeViewModel : ViewModel() {

    val darkModeUnitState: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}