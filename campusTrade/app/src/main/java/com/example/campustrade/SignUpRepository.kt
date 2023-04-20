package com.example.campustrade

import com.google.firebase.firestore.CollectionReference

class SignUpRepository: SignUpRepositoryInterface {

    private val firebaseAuth = FirebaseClient().auth
    private val firebaseFireStore = FirebaseClient().fireStore

    override suspend fun createUser(vt: String, nn: String, em: String, pw: String): Boolean {
        var aux = true
        firebaseAuth
            .createUserWithEmailAndPassword(em, pw)
            .addOnCompleteListener(){
                if (it.isSuccessful) {
                    //creating a collection reference for our Firebase FireStore database.
                    val dbUsers: CollectionReference = firebaseFireStore.collection("users")
                    //adding our data to our courses object class.
                    val userObj = UserObj(nn, em, vt,"https://firebasestorage.googleapis.com/v0/b/campustrade-6d7b6.appspot.com/o/images%2Fowl.jpg?alt=media&token=d9dd4852-dcad-4811-9739-36909d731a6d")
                    //below method is use to add data to Firebase FireStore.
                    dbUsers.add(userObj).addOnSuccessListener {
                        // after the data addition is successful
                        // we are displaying a success toast message.
                    }.addOnFailureListener { e ->
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