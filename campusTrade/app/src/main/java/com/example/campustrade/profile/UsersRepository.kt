package com.example.campustrade.profile

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.campustrade.dtos.UserObj
import com.example.campustrade.objects.CurrentUser
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
    fun updateDate(email: String, newDate: String, context: Context) {

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

                    val name = user.getString("name")
                    val tag = user.getString("tag")
                    val image = user.getString("image")

                    CurrentUser.user = UserObj(name!!, email, tag!!, image!!, newDate)

                    updateSharedPreferences(context)

                }
            }
    }

    fun updateSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("logged_user_preferences", Context.MODE_PRIVATE)

        val currentUser = CurrentUser.user

        val em  = currentUser!!.email
        val nn  = currentUser!!.name
        val tag  = currentUser!!.tag
        val url = currentUser!!.image

        val editor = sharedPreferences.edit()
        editor.putString("email", em)
        editor.putString("name", nn)
        editor.putString("tag", tag)
        editor.putString("image", url)
        editor.apply()

    }


}