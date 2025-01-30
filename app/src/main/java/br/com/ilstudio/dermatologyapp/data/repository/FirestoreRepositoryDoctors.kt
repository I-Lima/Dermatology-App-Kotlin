package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceDoctors

class FirestoreRepositoryDoctors {
    val firestoreServiceDoctors = FirestoreServiceDoctors()

    suspend fun getAll(): DoctorsResponse {
        return try {
            val result = firestoreServiceDoctors.getAll()
            val response = result.documents

            if (response.isEmpty()) {
                return DoctorsResponse(false, null, null, true)
            }

            val listData = response.map {
                val data = it.data ?: emptyMap<String, Any>()

                DoctorsData(
                    data["name"] as? String ?: "",
                    data["starts"] as? Int ?: 0,
                    data["profile"] as? String ?: "",
                    data["highlights"] as? String ?: "",
                    data["focus"] as? String ?: "",
                    data["expertise"] as? String ?: "",
                    data["experience"] as? Int ?: 0,
                    data["date"] as? String ?: "",
                    data["career path"] as? String ?: "",
                    data["comments"] as? Int ?: 0
                )
            }

            DoctorsResponse(true, listData, null, false)
        } catch (e: Exception) {
            DoctorsResponse(false, null, "Get doctors error")
        }
    }
}