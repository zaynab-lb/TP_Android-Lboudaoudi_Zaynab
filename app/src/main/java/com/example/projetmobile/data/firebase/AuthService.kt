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

    suspend fun updateUser(userId: String, updatedUser: User) {
        try {
            firestore.collection("users").document(userId).set(updatedUser).await()
        } catch (e: Exception) {
            Log.e("AuthService", "Erreur lors de la mise à jour de l'utilisateur", e)
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String?): Boolean {
        val user = auth.currentUser ?: return false
        val email = user.email ?: return false

        return try {
            // Re-authentifier l'utilisateur
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()

            // Mettre à jour le mot de passe
            if (!newPassword.isNullOrBlank()) {
                user.updatePassword(newPassword).await()
            }

            true
        } catch (e: Exception) {
            Log.e("AuthService", "Erreur mise à jour mot de passe : ${e.message}", e)
            false
        }
    }


    suspend fun getAllUsers(): List<User> {
        return try {
            firestore.collection("users").get().await().toObjects(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthService", "Erreur récupération utilisateurs", e)
            emptyList()
        }
    }



}
