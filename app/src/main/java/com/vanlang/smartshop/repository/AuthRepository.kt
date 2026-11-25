package com.vanlang.smartshop.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Không thể tạo tài khoản")

        val userData = hashMapOf(
            "uid" to user.uid,
            "fullName" to fullName,
            "email" to email,
            "phone" to "",
            "address" to "",
            "role" to "customer",
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userData)
            .await()

        return user
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Đăng nhập thất bại")
    }

    suspend fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun getUserProfile(uid: String): Map<String, Any>? {
        val document = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        return document.data
    }

    fun logout() {
        auth.signOut()
    }
}
