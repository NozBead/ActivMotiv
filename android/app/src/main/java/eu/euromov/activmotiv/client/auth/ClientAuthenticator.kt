package eu.euromov.activmotiv.client.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle

class ClientAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {

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

    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle {
        return Bundle()
    }

    override fun getAuthTokenLabel(authTokenType: String) : String {
        throw UnsupportedOperationException()
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<String>) : Bundle? {
        throw UnsupportedOperationException()
    }
}