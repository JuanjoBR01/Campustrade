package com.example.campustrade.publish

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class PublishScreen : ComponentActivity() {
    private val viewModel by viewModels<PublishViewModel>()

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
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        val param1 = intent.getStringExtra("imgUris")
        var uris: Uri? = null
        if (param1 != null) {
            uris = Uri.parse(param1)
        }
        setContent {
            PublishScreenV(uris,viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}
