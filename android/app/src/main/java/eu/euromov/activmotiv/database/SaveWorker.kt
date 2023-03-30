package eu.euromov.activmotiv.database

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.model.Unlock

class SaveWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val time = inputData.getLong("time",0)
        val exposureTime = inputData.getLong("exposureTime",0)
        val unlock = Unlock(time/1000, exposureTime, false)
        UnlockDatabase.getDatabase(applicationContext).unlockDao().insert(unlock)
        return Result.success()
    }
}