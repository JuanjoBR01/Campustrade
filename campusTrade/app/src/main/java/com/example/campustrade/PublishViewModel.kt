package com.example.campustrade

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import java.io.ByteArrayOutputStream
import java.util.*

class PublishViewModel :ViewModel(){

    //Atributo del nombre del producto a agregar
    private val _prodName= MutableLiveData<String>(" ")
    val prodName:LiveData<String> = _prodName

    //Atributo del precio del producto a agregar
    private val _prodPrice= MutableLiveData<String>("0")
    val prodPrice:LiveData<String> = _prodPrice

    //Atributo del description del producto a agregar
    private val _prodDescr= MutableLiveData<String>(" ")
    val prodDescr:LiveData<String> = _prodDescr

    //Atributo del Tags del producto a agregar
    private val _prodTags= MutableLiveData<String>(" ")
    val prodTags:LiveData<String> = _prodTags

    //Atributo de new o old del producto a agregar
    private val _selectedItem= MutableLiveData<String>(" ")
    val selectedItem:LiveData<String> = _selectedItem

    //Atributo de la ruta de la imagen del producto
    private val _contentImage= MutableLiveData<Uri?>()
    val contentImage:LiveData<Uri?> = _contentImage

    //Atributo del scope
    private val _scope= MutableLiveData<CoroutineScope>()
    val scope:LiveData<CoroutineScope> = _scope

    //Atributo de si esta expandido
    private val _expanded= MutableLiveData<Boolean>(false)
    val expanded:LiveData<Boolean> = _expanded

    //Tipo de producto
    private val _valueType = MutableLiveData<String>(" ")
    val valueType:LiveData<String> = _valueType

    //Cargando
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading:LiveData<Boolean> = _isLoading

    //Atributo del state
    @OptIn(ExperimentalMaterialApi::class)
    private val _state= MutableLiveData<ModalBottomSheetState>()
    @OptIn(ExperimentalMaterialApi::class)
    val state:LiveData<ModalBottomSheetState> = _state

    fun onPublishChanged(pName:String,pPrice:String,pDescrip:String,pTags:String,pSelectedIt:String){
        //Change value if restrictions are correct
        if(pName.length <= 30){
            _prodName.value = pName
        }

        if(pPrice.length <= 9){
            _prodPrice.value = pPrice
        }

        if(pDescrip.length <= 250){
            _prodDescr.value = pDescrip
        }

        if(pTags.length <= 100){
            _prodTags.value = pTags
        }

        _selectedItem.value = pSelectedIt
    }

    fun onChangeComboBox(pValueType:String, pExpanded:Boolean){
        _valueType.value = pValueType
        _expanded.value = pExpanded
    }

    fun onChangeImage(pContentImage:Uri?){
        _contentImage.value = pContentImage
    }

    fun uploadImageToDataBase(context: Context, productOb: ProductObj){

        // create the storage reference
        val storageRef = Firebase.storage.reference
        Log.d(ContentValues.TAG,"Create Storage Ref")

        //Transform to bitmap
        Log.d(ContentValues.TAG,"Content image" + _contentImage.value.toString())
        val inputStream = context.contentResolver.openInputStream(_contentImage.value!!)
        val bitmp: Bitmap = BitmapFactory.decodeStream(inputStream)
        Log.d(ContentValues.TAG,"Create Bitmap")


        //Bitmap to bytes
        val outputStream = ByteArrayOutputStream()
        bitmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        Log.d(ContentValues.TAG,"Bitmap to bytes")

        //Create variable to store url
        var imgUrl = ""

        //Upload to DB
        val storeR = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = storeR.putBytes(bytes)

        Log.d(ContentValues.TAG,"todo ok----------------------------------------------------------------------------------------------")

        uploadTask.addOnSuccessListener { taskSnapshot ->
            storeR.downloadUrl.addOnSuccessListener { uri ->
                imgUrl = uri.toString()
                productOb.changeImage(imgUrl)
                Log.d(ContentValues.TAG,"todo ok imagen----------------------------------------------------------------------------------------------")
                uploadProductToDB(productOb, context)
            }
        }
    }

    private fun uploadProductToDB(productOb: ProductObj, context: Context) {
        // on below line creating an instance of firebase firestore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //creating a collection reference for our Firebase Firestore database.
        val dbCourses: CollectionReference = db.collection("ProductsDB")

        Log.d(ContentValues.TAG,"todo ok prod1----------------------------------------------------------------------------------------------")

        //adding our data to our courses object class.
        //below method is use to add data to Firebase Firestore.
        dbCourses.add(productOb).addOnSuccessListener {
            // after the data addition is successful
            // we are displaying a success toast message.
            Log.d(ContentValues.TAG, "Subio----------------------------------------------------------------------")
            Toast.makeText(
                context,
                "Your Product has been added to Firebase Firestore",
                Toast.LENGTH_LONG
            ).show()
            onChangeIsLoading(false)
        }.addOnFailureListener { e ->
            // this method is called when the data addition process is failed.
            // displaying a toast message when data addition is failed.
            Toast.makeText(context, "Fail to add product", Toast.LENGTH_LONG).show()
            onChangeIsLoading(false)
        }
    }

    fun onChangeIsLoading(pLoading:Boolean){
        _isLoading.value = pLoading
    }
}