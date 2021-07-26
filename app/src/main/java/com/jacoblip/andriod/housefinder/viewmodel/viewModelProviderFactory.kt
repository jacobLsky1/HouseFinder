package com.jacoblip.andriod.housefinder.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jacoblip.andriod.housefinder.repository.Repository

class viewModelProviderFactory(
    val repository: Repository,
    val context: Context
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository,context) as T
    }
}