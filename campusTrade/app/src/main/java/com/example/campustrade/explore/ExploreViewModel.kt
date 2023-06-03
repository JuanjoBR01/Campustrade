package com.example.campustrade.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.campustrade.dtos.Distributor
import com.example.campustrade.objects.Coordinates
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
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

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude

                Coordinates.userCoordinates = LatLng(latitude, longitude)
                Coordinates.userCity = getCityName(latitude, longitude, context)
                Coordinates.userNeighbourhood = getNeighbourhoodName(latitude, longitude, context)
            }
        }


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

    fun restartState() {
        _loginFlow.value = Resource.PastFailure
    }

}



fun getCityName(latitude: Double, longitude: Double, context: Context): String {
    var cityName = ""
    val geocoder = Geocoder(context)


    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            cityName = addresses[0].locality ?: ""
        }
    }

    return cityName
}

fun getNeighbourhoodName(latitude: Double, longitude: Double, context: Context): String {
    var neighbourhoodName = ""
    val geocoder = Geocoder(context)


    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            neighbourhoodName = addresses[0].subLocality ?: ""
        }
    }

    return neighbourhoodName
}
