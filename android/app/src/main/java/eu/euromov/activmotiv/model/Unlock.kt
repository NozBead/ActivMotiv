package eu.euromov.activmotiv.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Unlock (
    @PrimaryKey(true)
    val id : Int,
    val time : Long,
    val exposureTime : Long,
    val sent : Boolean
)