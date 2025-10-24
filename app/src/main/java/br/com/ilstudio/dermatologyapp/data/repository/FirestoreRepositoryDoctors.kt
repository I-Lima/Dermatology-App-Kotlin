package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsResponse
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceDoctors
import com.google.android.gms.tasks.Task
import java.util.Calendar

class FirestoreRepositoryDoctors {
    private val firestoreServiceDoctors = FirestoreServiceDoctors()

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
                    data["type"] as? String ?: "male",
                    data["favorite"] as? Boolean ?: false
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
                val experienceData = data["experience"] as Long
                val currentYear = Calendar.getInstance()[Calendar.YEAR]
                val experience = currentYear - experienceData

                DoctorsDetailsData(
                    data["uid"] as? String ?: "",
                    data["starts"] as? Long ?: 0,
                    data["profile"] as? String ?: "",
                    data["highlights"] as? String ?: "",
                    data["focus"] as? String ?: "",
                    experience,
                    data["date"] as? String ?: "",
                    data["hour"] as? String ?: "",
                    data["career path"] as? String ?: "",
                    data["comments"] as? Long ?: 0,
                )
            }

            DoctorsDetailsResponse(true, listData.get(0), null, false)
        } catch (e: Exception) {
            DoctorsDetailsResponse(false, null, "Get doctor details error")
        }
    }

    fun updateFavoriteDoctor(id: String, favorite: Boolean): Task<Void> {
        return firestoreServiceDoctors.updateFavoriteDoctor(id, favorite)
    }
}
