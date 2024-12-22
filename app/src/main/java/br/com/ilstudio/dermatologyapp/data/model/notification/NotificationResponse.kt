package br.com.ilstudio.dermatologyapp.data.model.notification

data class NotificationResponse(
    val success: Boolean,
    val data: List<NotificationsData>? = null,
    val message: String? = null,
    val isEmpty: Boolean = false
)
