package ru.vanchikov.fitnesinmylife.data.model

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Entity(tableName = "users_table")
data class LoggedInUser(
    @PrimaryKey @ColumnInfo(name = "userId") val userId: String,
    val displayName: String,
    val password : String,
    val email: String
)
