package com.example.campustrade.prodsProfile

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.campustrade.home.HomeViewModel


class ProdsPScreenActivity : ComponentActivity() {
    private val viewModel by viewModels<ProdsPViewModel>()



    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.onNetworkStateChanged(true)
        }

        override fun onLost(network: Network) {
            viewModel.onNetworkStateChanged(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onInicialization()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        setContent {
            ProdsPScreenMain(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


}