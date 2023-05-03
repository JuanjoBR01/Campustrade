package com.example.campustrade.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.campustrade.dtos.UserObj
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class UsersRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getData(): List<UserObj> {
        val collec = db.collection("users")
        val querySnapshot = collec.get().await()
        return querySnapshot.toObjects<UserObj>()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateDate(email: String, newDate: String) {

        val collec = db.collection("users")
        val query = collec.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { users ->
                for (user in users) {
                    val id = user.id

                    val newData = hashMapOf(
                        "lastaccess" to newDate,
                    )
                    collec.document(id).update(newData as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.d("Last access date", "The date was updated successfully")
                        }
                        .addOnFailureListener {
                            Log.d("Last access date", "The date was updated successfully")
                        }

                }
            }
    }


}