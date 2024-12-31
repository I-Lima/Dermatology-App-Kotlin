package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreServiceNotifications {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getNotifications(userId: String): QuerySnapshot {
        return db.collection(NOTIFICATIONS_TABLE)
            .whereEqualTo("user_uid", userId)
            .get()
            .await()
    }

    companion object {
        private const val NOTIFICATIONS_TABLE = "notifications"
    }
}
