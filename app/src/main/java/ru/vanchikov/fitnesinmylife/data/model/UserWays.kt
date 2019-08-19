package ru.vanchikov.fitnesinmylife.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users_ways_table", foreignKeys = [ForeignKey(
    entity = LoggedInUser::class,
    parentColumns = ["userId"],
    childColumns = ["userId"],
    onDelete = ForeignKey.CASCADE
)])
data class UserWays(
    @PrimaryKey(autoGenerate = true) val wayId: Long,
    val userId: String,
    val coordinate : Long


)



/*

    @ForeignKey(entity = LoggedInUser::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE) @ColumnInfo(name = "userId")

 */