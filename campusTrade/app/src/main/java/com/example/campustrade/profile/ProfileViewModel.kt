package com.example.campustrade.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campustrade.dtos.UserObj

class ProfileViewModel: ViewModel() {

    private val repository: UsersRepository = UsersRepository()

    private val _userObj = MutableLiveData<UserObj>()
    val userObj: LiveData<UserObj> = _userObj



}