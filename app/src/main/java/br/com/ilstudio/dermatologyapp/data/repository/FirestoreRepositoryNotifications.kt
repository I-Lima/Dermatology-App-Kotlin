package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.notification.NotificationResponse
import br.com.ilstudio.dermatologyapp.data.model.notification.NotificationsData
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceNotifications

class FirestoreRepositoryNotifications {
    private val firestoreServiceNotifications = FirestoreServiceNotifications()

    /**
     * Retrieves notifications for a specific user from the Firestore database.
     *
     * This function fetches notification data for the given `userId` using the `firestoreServiceNotifications`.
     * If notifications are found, it maps the retrieved documents into a list of [NotificationsData] objects
     * and returns a [NotificationResponse] containing the data. If no notifications are found, it returns
     * a [NotificationResponse] indicating that the notifications list is empty. If an error occurs during
     * the retrieval process, it returns a failure response with an error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param userId The unique identifier of the user for whom to fetch notifications.
     * @return A [NotificationResponse] containing the success status, the list of notifications (if found),
     *         and any applicable error message or metadata.
     */
    suspend fun getNotifications(userId: String): NotificationResponse {
        return try {
            val response = firestoreServiceNotifications.getNotifications(userId)
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
