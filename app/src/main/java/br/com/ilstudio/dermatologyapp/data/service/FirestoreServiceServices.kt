package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreServiceServices {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getAll(): DocumentSnapshot? {
        return db.collection(SERVICES_TABLE)
            .document()
            .get()
            .await()
    }

    companion object {
        private const val SERVICES_TABLE = "services"
    }
}
