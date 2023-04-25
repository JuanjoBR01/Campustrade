package com.example.campustrade.publish

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class PublishScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val param1 = intent.getStringExtra("imgUris")
        var uris: Uri? = null
        if (param1 != null) {
            uris = Uri.parse(param1)
        }
        setContent {
            PublishScreenV(uris, PublishViewModel())
        }
    }
}