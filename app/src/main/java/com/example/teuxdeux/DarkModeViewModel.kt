package com.example.teuxdeux
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DarkModeViewModel : ViewModel() {

    val darkModeUnitState: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}