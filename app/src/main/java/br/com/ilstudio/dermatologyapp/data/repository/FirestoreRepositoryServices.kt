package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.services.ServicesData
import br.com.ilstudio.dermatologyapp.data.model.services.ServicesResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceServices

class FirestoreRepositoryServices {
    private val firestoreServiceServices = FirestoreServiceServices()

    suspend fun getAllServices(): ServicesResponse {
        return try {
            val response = firestoreServiceServices.getAll()?.documents

            if (response == null) {
                return ServicesResponse(false, null, null, true)
            }

            val listData = response.map {
                val data = it.data ?: emptyMap<String, Any>()

                ServicesData(
                    data["uid"] as? String ?: "",
                    data["name"] as? String ?: "",
                    data["price"] as? Double ?: 0.0,
                )
            }

            ServicesResponse(true, listData)
        } catch (e: Exception) {
            ServicesResponse(success = false, message = "Error fetching services.")
        }
    }
}
