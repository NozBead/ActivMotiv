package eu.euromov.activmotiv.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore

data class UnlockDay (
    val dayOfWeek : Int,
    val numberOfUnlocks : Int
)