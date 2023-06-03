package com.example.campustrade.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.campustrade.dtos.Distributor
import com.example.campustrade.dtos.UserObj
import com.example.campustrade.objects.CurrentUser
import com.example.campustrade.objects.SignUpTime
import com.example.campustrade.objects.TimeOfLaunch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

class TelemetryRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadLaunchTime() {

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
    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadSignUpTime() {

        val collec = db.collection("SignUpTimes")

        val accessDate = LocalDateTime.now()
        val accessString = "${accessDate.dayOfMonth}/${accessDate.monthValue}/${accessDate.year}"

        val signuptime = hashMapOf(
            "Date" to accessString,
            "Time" to SignUpTime.totalTime / 1000
        )

        collec.add(signuptime).addOnSuccessListener { documentReference ->
            println("Documento agregado con ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                println("Error al agregar el documento: $e")
            }

    }



    fun updateVisits(menuBar: String) {

        var visits = 0L

        db.collection("Functionalities")
            .document(menuBar)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    visits = document.get("Visits") as Long
                }
            }

        val upvisits = visits + 1

        val newData = hashMapOf(
            "Visits" to upvisits,
        )

        db.collection("Functionalities")
            .document(menuBar)
            .set(newData)
            .addOnSuccessListener {
                Log.d("Update","Visits updated")
            }
    }



}