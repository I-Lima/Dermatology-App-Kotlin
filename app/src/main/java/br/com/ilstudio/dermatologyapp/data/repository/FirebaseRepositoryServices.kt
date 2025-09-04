package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.services.ServicesResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceServices

class FirebaseRepositoryServices {
    private val firestoreServiceServices = FirestoreServiceServices()

    suspend fun getAllServices(): ServicesResponse {
        return try {
            val response = firestoreServiceServices.getAll()

            //TODO: Handle response

            ServicesResponse(success = false, message = "Error fetching services.")
        } catch (e: Exception) {
            ServicesResponse(success = false, message = "Error fetching services.")
        }
    }
}
