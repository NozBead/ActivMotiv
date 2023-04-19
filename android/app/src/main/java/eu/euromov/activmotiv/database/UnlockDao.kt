package eu.euromov.activmotiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.euromov.activmotiv.model.Stats
import eu.euromov.activmotiv.model.Unlock

@Dao
interface UnlockDao {
    @Query("SELECT * FROM unlock WHERE sent = 0")
    fun getAllNotSent() : List<Unlock>

    @Query("SELECT count(*) FROM unlock GROUP BY date(time, 'unixepoch')")
    fun getUnlockByDay() : List<Int>

    @Query("SELECT count(*) AS unlockNumber, sum(exposureTime) AS totalExposure FROM unlock")
    fun getStat() : Stats

    @Insert
    fun insert(vararg unlock : Unlock)

    @Update
    fun update(unlock: Unlock)
}