package com.example.campustrade.signup

import com.example.campustrade.objects.FirebaseClient
import com.example.campustrade.dtos.UserObj
import com.google.firebase.firestore.CollectionReference

class SignUpRepository: SignUpRepositoryInterface {

    private val firebaseAuth = FirebaseClient.auth
    private val firebaseFireStore = FirebaseClient.fireStore

    override suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Boolean {
        var aux = true
        firebaseAuth
            .createUserWithEmailAndPassword(em, pw)
            .addOnCompleteListener(){
                if (it.isSuccessful) {
                    //creating a collection reference for our Firebase FireStore database.
                    val dbUsers: CollectionReference = firebaseFireStore.collection("users")
                    //adding our data to our courses object class.
                    val userObj = UserObj(nn, em, vt,imgUrl)
                    //below method is use to add data to Firebase FireStore.
                    dbUsers.add(userObj).addOnSuccessListener {
                        // after the data addition is successful
                        // we are displaying a success toast message.
                    }.addOnFailureListener { _ ->
                        // this method is called when the data addition process is failed.
                        // displaying a toast message when data addition is failed.
                        aux = false
                    }
                } else {
                    aux = false
                }
            }

        return aux
    }

}