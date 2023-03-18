package eu.euromov.activmotiv.model

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Instant {
        return value.let { Instant.ofEpochSecond(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant): Long {
        return date.epochSecond
    }
}
