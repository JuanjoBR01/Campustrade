package com.example.campustrade.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.campustrade.dtos.UserObj
import com.example.campustrade.objects.CurrentUser
import com.example.campustrade.objects.TimeOfLaunch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class TelemetryRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadLaunchTime() {

        val collec = db.collection("LaunchTimes")

        val accessDate = LocalDateTime.now()
        val accessString = "${accessDate.dayOfMonth}/${accessDate.monthValue}/${accessDate.year}"

        val launchTime = hashMapOf(
            "Date" to accessString,
            "Time" to TimeOfLaunch.time
        )

        collec.add(launchTime).addOnSuccessListener { documentReference ->
            println("Documento agregado con ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                println("Error al agregar el documento: $e")
            }




    }



}