package eu.euromov.activmotiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.euromov.activmotiv.model.Unlock

@Dao
interface UnlockDao {
    @Query("SELECT * FROM unlock WHERE sent = 0")
    fun getAllNotSent() : List<Unlock>

    @Insert
    fun insert(vararg unlock : Unlock)

    @Update
    fun update(unlock: Unlock)
}