package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreServiceServices {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getAll(): QuerySnapshot? {
        return db.collection(SERVICES_TABLE)
            .get()
            .await()
    }

    companion object {
        private const val SERVICES_TABLE = "services"
    }
}
