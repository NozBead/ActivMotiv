package eu.euromov.activmotiv.client.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.ClientCallback
import eu.euromov.activmotiv.client.UploadClient
import java.security.MessageDigest
import java.util.*


class ClientAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {
    private val am: AccountManager = AccountManager.get(context)

    override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String, requiredFeatures: Array<String>?, options: Bundle?) : Bundle {
        val bundle = Bundle()
        val intent = Intent(context, RegisterAccountActivity::class.java)
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account, options: Bundle) : Bundle {
        throw UnsupportedOperationException()
    }
    
    override fun updateCredentials(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle? {
        throw UnsupportedOperationException()
    }

    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String) : Bundle? {
        throw UnsupportedOperationException()
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle? {
        val client = UploadClient.getClient(context.getString(R.string.server))
        val sessionCookie : String? = am.getUserData(account, AccountManager.KEY_AUTHTOKEN)

        val base = Base64.getUrlEncoder()
        val md = MessageDigest.getInstance("SHA-256")
        val password = am.getPassword(account).toByteArray()
        val passwordDigest = base.encodeToString(md.digest(password))

        val credentials = account.name + ":" + passwordDigest
        val basic = base.encodeToString(credentials.toByteArray())
        val callback = ClientCallback {
            val bundle = Bundle()
            if (it.code() == 200) {
                bundle.putString(AccountManager.KEY_AUTHTOKEN, it.headers().get("Cookie"))
                am.setUserData(account, AccountManager.KEY_AUTHTOKEN, it.headers().get("Cookie"))
            }
            response.onResult(bundle)
        }

        if (sessionCookie == null) {
            client.login("Basic $basic").enqueue(callback)
        }
        else {
            client.check(sessionCookie).enqueue(callback)
        }

        return null
    }

    override fun getAuthTokenLabel(authTokenType: String) : String {
        throw UnsupportedOperationException()
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<String>) : Bundle? {
        throw UnsupportedOperationException()
    }
}