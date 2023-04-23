package eu.euromov.activmotiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.euromov.activmotiv.model.Stats
import eu.euromov.activmotiv.model.Unlock
import eu.euromov.activmotiv.model.UnlockDay

@Dao
interface UnlockDao {
    @Query("SELECT * FROM unlock WHERE sent = 0")
    fun getAllNotSent() : List<Unlock>

    @Query("SELECT strftime('%w', time, 'unixepoch', 'localtime') AS dayOfWeek, count(*) AS numberOfUnlocks FROM unlock WHERE time >= :time GROUP BY dayOfWeek")
    fun getUnlockByDay(time: Long) : List<UnlockDay>

    @Query("SELECT count(*) AS unlockNumber, sum(exposureTime) AS totalExposure FROM unlock")
    fun getStat() : Stats

    @Insert
    fun insert(vararg unlock : Unlock)

    @Update
    fun update(unlock: Unlock)
}