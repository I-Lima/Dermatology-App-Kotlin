package br.com.ilstudio.dermatologyapp.data.model.user

class NewAccount (
    val uid: String,
    val mobileNumber: String,
    val dateBirth: String
) {
    /**
     * Converts the user object to a map representation.
     *
     * This function transforms the user object's properties into a map where each key corresponds
     * to the property name and each value corresponds to the property value. This map can be useful
     * for serialization, such as when saving user data to a database or sending it over a network.
     *
     * @return A [Map] containing the user's properties with their corresponding values, where keys are
     *         the property names and values are the property values.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "mobileNumber" to mobileNumber,
            "dateBirth" to dateBirth,
        )
    }
}
