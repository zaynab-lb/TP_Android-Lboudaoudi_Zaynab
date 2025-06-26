package com.example.projetmobile.data.firebase

import android.util.Log
import com.example.projetmobile.data.Entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUpClient(user: User, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val userId = result.user?.uid ?: return false
            val newUser = user.copy(id = userId, role = "client")
            firestore.collection("users").document(userId).set(newUser).await()
            true
        } catch (e: Exception) {
            Log.e("AuthService", "Erreur lors de l'inscription : ${e.message}", e)
            false
        }
    }

    suspend fun login(email: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return null
            firestore.collection("users").document(userId)
                .get().await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            firestore.collection("users").document(userId)
                .get().await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
