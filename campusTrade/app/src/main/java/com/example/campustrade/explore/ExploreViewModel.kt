package com.example.campustrade.explore

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.campustrade.dtos.Distributor
import com.example.campustrade.objects.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.lang.Exception


class ExploreViewModel (): ViewModel(){

    private val repository = ExploreRepository()

    private val _distributors = MutableStateFlow<List<Distributor>?>(null)
    val distributors: StateFlow<List<Distributor>?> = _distributors

    private val _mapButtonEnabled = MutableLiveData(true)
    val mapButtonEnabled: LiveData<Boolean> = _mapButtonEnabled

    private val _loginFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val loginFlow: StateFlow<Resource<Boolean>?> = _loginFlow

    fun getDistributors(context: Context) = viewModelScope.launch {
        _mapButtonEnabled.value = false

        withContext(Dispatchers.IO) {
            val result = repository.getDistributors()
            _distributors.value = result

            if (result.isNotEmpty()) {
                Coordinates.distributors = result
                _loginFlow.value = Resource.Success(true)
            } else {
                _loginFlow.value = Resource.Failure(Exception("The distributors couldn't be retrieved!"))
            }
        }
        _mapButtonEnabled.value = true

    }

}
