package com.example.campustrade.explore

import android.content.Context
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.campustrade.data.Resource
import com.example.campustrade.dtos.UserObj
import com.example.campustrade.objects.CurrentUser
import com.example.campustrade.profile.UsersRepository
import com.example.campustrade.repository.AuthRepository
import com.example.campustrade.repository.TelemetryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ExploreViewModel (

):
    ViewModel(){


    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow




}
