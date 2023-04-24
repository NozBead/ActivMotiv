package eu.euromov.activmotiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.euromov.activmotiv.model.Image
import eu.euromov.activmotiv.model.ImageType
import eu.euromov.activmotiv.model.Stats
import eu.euromov.activmotiv.model.Unlock
import eu.euromov.activmotiv.model.UnlockDay

@Dao
interface ImageDao {
    @Query("SELECT * FROM image WHERE type = :type")
    fun getAllOfType(type: ImageType) : List<Image>

    @Insert
    fun insert(vararg image : Image)

    @Query("SELECT EXISTS(SELECT * FROM image WHERE name = :name)")
    fun exists(name: String) : Boolean

    @Update
    fun update(image: Image)
}