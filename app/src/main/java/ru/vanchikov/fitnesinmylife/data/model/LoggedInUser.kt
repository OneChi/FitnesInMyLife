package ru.vanchikov.fitnesinmylife.data.model

import android.provider.ContactsContract

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val password : String,
    val email: String
)
