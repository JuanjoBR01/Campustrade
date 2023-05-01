package com.example.campustrade

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ConnectivityReceiver(private val context: Context) : BroadcastReceiver() {

    var isOnline by mutableStateOf(true)

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        isOnline = !(networkInfo == null || !networkInfo.isConnected)
    }

    fun register() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(this, intentFilter)
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }
}