package eu.euromov.activmotiv.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
data class Unlock (
    @PrimaryKey
    val time : Long,
    val exposureTime : Long,
    @JsonIgnore
    var sent : Boolean
)