package com.example.campustrade.repository

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.dtos.UsersProdsObj
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class PublishRepository : RepositoryInterface {

    override suspend fun uploadImageToDataBase(
        productOb: ProductObj,
        bitmp: Bitmap
    ): ProductObj {

        // create the storage reference
        val storageRef = Firebase.storage.reference

        //Bitmap to bytes
        val outputStream = ByteArrayOutputStream()
        bitmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        //Create variable to store url
        var imgUrl = ""

        //Upload to DB
        val storeR = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = storeR.putBytes(bytes)

        try {
            uploadTask.await()
            imgUrl = storeR.downloadUrl.await().toString()
            productOb.changeImage(imgUrl)
        }
        catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return productOb
    }

    override suspend fun uploadProductToDB(productOb: ProductObj): Boolean {

        // on below line creating an instance of firebase firestore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        //creating a collection reference for our Firebase Firestore database.
        val dbCourses: CollectionReference = db.collection("ProductsDB")

        //Success or not
        val deferred = CompletableDeferred<Boolean>()

        //below method is use to add data to Firebase Firestore.
        dbCourses.add(productOb).addOnSuccessListener {
            deferred.complete(true)
        }.addOnFailureListener{
            deferred.complete(false)
        }
        return deferred.await()
    }

    override suspend fun createUser(
        vt: String,
        nn: String,
        em: String,
        pw: String,
        imgUrl: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getData(): List<ProductDB> {
        TODO("Not needed")
    }

    override suspend fun getDataP(): List<ProductObj> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyObjects(): List<UsersProdsObj> {
        TODO("Not yet implemented")
    }

    override suspend fun makeLogin(email: String, password: String): Boolean {
        TODO("Not needed")
    }

}