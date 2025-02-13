package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreServiceDoctors {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getAll(): QuerySnapshot {
        return db.collection(DOCTORS_TABLE)
            .get()
            .await()
    }

    internal suspend fun getDoctorDetails(id: String): QuerySnapshot {
        return db.collection(DOCTORS_DETAILS_TABLE)
            .whereEqualTo("uid", id)
            .get()
            .await()
    }

    companion object {
        private const val DOCTORS_TABLE = "doctors"
        private const val DOCTORS_DETAILS_TABLE = "doctors_details"
    }
}