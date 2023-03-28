package eu.euromov.activmotiv.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Unlock (
    @PrimaryKey
    val time : Instant,
    val exposureTime : Long,
    val sent : Boolean
)