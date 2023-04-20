package com.example.campustrade

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaunchCameraViewModel: ViewModel(){

    //Atributo de la ruta de la imagen del producto
    private val _contentImageLC= MutableLiveData<Uri?>(null)
    val contentImageLC: LiveData<Uri?> = _contentImageLC

    fun onChangeContentImg(pContentImg:Uri?){
        _contentImageLC.value = pContentImg
    }

}