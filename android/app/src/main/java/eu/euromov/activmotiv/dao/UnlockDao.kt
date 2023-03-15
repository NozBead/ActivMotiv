package eu.euromov.activmotiv.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import eu.euromov.activmotiv.model.Unlock

@Dao
interface UnlockDao {
    @Query("SELECT * FROM unlock WHERE sent = 0")
    fun getAllNotSent() : List<Unlock>

    @Insert
    fun insert(vararg unlock : Unlock)
}