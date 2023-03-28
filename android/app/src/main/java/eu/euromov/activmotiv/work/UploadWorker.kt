package eu.euromov.activmotiv.work

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val am: AccountManager = AccountManager.get(applicationContext)
        val accounts: Array<Account> = am.getAccountsByType("eu.euromov.activmotiv")

        if(accounts.isEmpty()) {
            return Result.failure()
        }

        val account = accounts[0]
        return Result.success()
    }
}