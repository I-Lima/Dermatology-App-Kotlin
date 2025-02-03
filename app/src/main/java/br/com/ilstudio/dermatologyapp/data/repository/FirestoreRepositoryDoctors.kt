package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsResponse
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
                    data["uid"] as? String ?: "",
                    data["name"] as? String ?: "",
                    data["expertise"] as? String ?: "",
                    data["photo"] as? Long ?: 0,
                )
            }

            DoctorsResponse(true, listData, null, false)
        } catch (e: Exception) {
            DoctorsResponse(false, null, "Get doctors error")
        }
    }

    suspend fun getDoctorDetails(id: String): DoctorsDetailsResponse {
        return try {
            val result = firestoreServiceDoctors.getDoctorDetails(id)
            val response = result.documents

            if (response.isEmpty()) {
                return DoctorsDetailsResponse(false, null, null, true)
            }

            val listData = response.map {
                val data = it.data ?: emptyMap<String, Any>()

                DoctorsDetailsData(
                    data["uid"] as? String ?: "",
                    data["starts"] as? Long ?: 0,
                    data["profile"] as? String ?: "",
                    data["highlights"] as? String ?: "",
                    data["focus"] as? String ?: "",
                    data["experience"] as? Long ?: 0,
                    data["date"] as? String ?: "",
                    data["careerPath"] as? String ?: "",
                    data["comments"] as? Long ?: 0,
                )
            }

            DoctorsDetailsResponse(true, listData, null, false)
        } catch (e: Exception) {
            DoctorsDetailsResponse(false, null, "Get doctor details error")
        }
    }
}