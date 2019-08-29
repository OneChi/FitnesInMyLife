package ru.vanchikov.fitnesinmylife.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that captures user information for logged in users retrieved from DataRepository
 */
@Entity(tableName = "users_table")
data class LoggedInUser(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "userId")  val userId: Long,
    val displayName: String,
    val password : String,
    val email: String

)
