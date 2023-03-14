package eu.euromov.activemotiv.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.euromov.activemotiv.dao.UnlockDao
import eu.euromov.activemotiv.model.Unlock

@Database(entities = [Unlock::class], version = 1)
abstract class UnlockDatabase : RoomDatabase() {
    abstract fun unlockDao(): UnlockDao
}
