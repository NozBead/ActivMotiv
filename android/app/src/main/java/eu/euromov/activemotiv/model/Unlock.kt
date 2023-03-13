package eu.euromov.activemotiv.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Unlock (
    @PrimaryKey(true)
    val id : Int,
    val time : LocalDateTime,
    val duration : Double
)