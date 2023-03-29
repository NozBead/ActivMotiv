package eu.euromov.activmotiv.client

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.work.Worker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.database.UnlockDatabase

class UploadWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val am: AccountManager = AccountManager.get(applicationContext)
        val accounts: Array<Account> = am.getAccountsByType(applicationContext.getString(R.string.accountType))

        if(accounts.isEmpty()) {
            return Result.failure()
        }

        val account = accounts[0]
        am.getAuthToken(
            account,
            "",
            Bundle(),
            true,
            {
                val bundle: Bundle = it.result
                val token: String = bundle.getString(AccountManager.KEY_AUTHTOKEN,"")
                upload(token)
            },
            null
        )


        return Result.success()
    }

    private fun upload(sessionCookie: String) {
        val unlocks = UnlockDatabase.getDatabase(applicationContext).unlockDao()
        val client = UploadClient.getClient(applicationContext.getString(R.string.server))
        val notSent = unlocks.getAllNotSent()

        for (unlock in notSent) {
            val callback = ClientCallback {
                if (it.code() == 200) {
                    unlock.sent = true
                    unlocks.update(unlock)
                }
            }
            client.unlock(sessionCookie, unlock).enqueue(callback)
        }
    }
}