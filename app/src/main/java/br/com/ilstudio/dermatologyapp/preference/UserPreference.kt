package br.com.ilstudio.dermatologyapp.preference

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import br.com.ilstudio.dermatologyapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.sql.Timestamp

private val Context.dataStore by preferencesDataStore("user_prefs")

object UserPreference {
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_BIRTHDATE = stringPreferencesKey("user_birthdate")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_PHONE = stringPreferencesKey("user_phone")
    private val USER_PROFILE_PICTURE = stringPreferencesKey("user_profile_picture")
    private val USER_GENDER = stringPreferencesKey("user_gender")

    suspend fun saveUser(
        context: Context,
        id: String,
        name: String,
        birthdate: Timestamp? = null,
        email: String,
        phone: String,
        profilePicture: String? = null,
        gender: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[USER_NAME] = name
            prefs[USER_BIRTHDATE] = birthdate.toString()
            prefs[USER_EMAIL] = email
            prefs[USER_PHONE] = phone
            prefs[USER_PROFILE_PICTURE] = profilePicture ?: ""
            prefs[USER_GENDER] = gender
        }
    }

    fun getUser(context: Context): Flow<User?> {
        return context.dataStore.data.map { prefs ->
            val id = prefs[USER_ID]

            if (id == null) {
                null
            } else {
                User(
                    id = id,
                    name = prefs[USER_NAME] ?: "",
                    email = prefs[USER_EMAIL] ?: "",
                    mobileNumber = prefs[USER_PHONE] ?: "",
                    dateBirth = prefs[USER_BIRTHDATE] ?: "",
                    profilePicture = prefs[USER_PROFILE_PICTURE],
                    gender = prefs[USER_GENDER] ?: "Male"
                )
            }
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
