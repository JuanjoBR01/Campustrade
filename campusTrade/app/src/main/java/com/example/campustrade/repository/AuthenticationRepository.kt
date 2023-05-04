package com.example.campustrade.repository

import com.example.campustrade.data.Resource
import com.example.campustrade.data.database.daos.UserDao
import com.example.campustrade.data.database.entities.UserEntity
import com.example.campustrade.data.utils.await
import com.example.campustrade.dtos.UserObj
import com.example.campustrade.dtos.toDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    //private val userDao: UserDao
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    /*
    suspend fun getAllUsersFromLocalDatabase(): List<UserObj> {
        val response: List<UserEntity> = userDao.gerAllUsers()
        return response.map{
            it.toDomain()
        }
    }

    suspend fun insertQuotes(quotes: List<UserEntity>) {
        userDao.insertAll(quotes)
    }

    suspend fun clearUsers() {
        userDao.deleteAll()
    }
*/
}