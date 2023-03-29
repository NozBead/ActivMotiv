package eu.euromov.activmotiv.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

@Entity
data class Unlock (
    @PrimaryKey
    val time : Instant,
    val exposureTime : Long,
    @JsonIgnore
    var sent : Boolean
)