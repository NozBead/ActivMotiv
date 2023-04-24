package eu.euromov.activmotiv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image (
    @PrimaryKey
    val name: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val file: ByteArray,
    val type: ImageType,
    var favorite: Boolean,
    var comment: String?
)