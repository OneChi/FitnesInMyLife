package ru.vanchikov.fitnesinmylife.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "wayfix_table",foreignKeys = [ForeignKey(
    entity = UserWays::class,
    parentColumns = ["wayId"],
    childColumns = ["wayId"],
    onDelete = ForeignKey.CASCADE
)])
data class WayFix (
    val wayId : Long,
    val altitude : Double,
    val latitude : Double,
    val longtitude : Double,
    val time : Long,
    val speed : Float,
    val provider: String,
    @PrimaryKey(autoGenerate = true) val fixId : Long
)