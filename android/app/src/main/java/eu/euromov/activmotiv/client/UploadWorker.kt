package eu.euromov.activmotiv.client

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.database.UnlockDatabase

class UploadWorker (appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val unlocks = UnlockDatabase.getDatabase(applicationContext).unlockDao()
    private val client = UploadClient.getClient(applicationContext.getString(R.string.server))
    private val am: AccountManager = AccountManager.get(applicationContext)
    private fun getToken(account : Account): String {
        return am.getAuthToken(
            account,
            "",
            null,
            true,
            null,
            null
        ).result.getString(AccountManager.KEY_AUTHTOKEN, "")
    }
    override suspend fun doWork(): Result {
        val accounts: Array<Account> = am.getAccountsByType(applicationContext.getString(R.string.accountType))

        if(accounts.isEmpty()) {
            return Result.failure()
        }

        val account = accounts[0]

        var token: String = getToken(account)
        if (!checkToken(token)) {
            am.invalidateAuthToken(applicationContext.getString(R.string.accountType), token)
            token = getToken(account)
            upload(token)
        }
        else {
            upload(token)
        }

        return Result.success()
    }

    private fun checkToken(token : String): Boolean {
        return client.check(token).execute().code() == 200
    }

    private fun upload(sessionCookie: String) {
        val notSent = unlocks.getAllNotSent()

        for (unlock in notSent) {
            val response = client.unlock(sessionCookie, unlock).execute()
            if (response.code() == 200) {
                unlock.sent = true
                unlocks.update(unlock)
            }
        }
    }
}