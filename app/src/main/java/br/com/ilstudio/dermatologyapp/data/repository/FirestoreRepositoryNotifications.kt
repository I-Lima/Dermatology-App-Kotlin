package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.notification.NotificationResponse
import br.com.ilstudio.dermatologyapp.data.model.notification.NotificationsData
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceNotifications

class FirestoreRepositoryNotifications {
    private val firestoreServiceNotifications = FirestoreServiceNotifications()

    suspend fun getNotifications(): NotificationResponse {
        return try {
            val response = firestoreServiceNotifications.getNotifications()
            val documents = response.documents

            if (response.documents.isEmpty()) {
                return NotificationResponse(false, null, null, true)
            }

            val listData = documents.map {
                val data = it.data ?: emptyMap<String, Any>()

                NotificationsData(
                    data["uid"] as? String ?: "",
                    data["title"] as? String ?: "",
                    data["type"] as? String ?: "",
                    data["description"] as? String ?: "",
                    data["date"] as? Long ?: 0
                )
            }

            NotificationResponse(true, listData)
        } catch (e: Exception) {
            NotificationResponse(false, null, "Get notifications error")
        }
    }

}
