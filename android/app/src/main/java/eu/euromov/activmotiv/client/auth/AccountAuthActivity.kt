package eu.euromov.activmotiv.client.auth

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.ClientCallback
import eu.euromov.activmotiv.client.UploadClient
import eu.euromov.activmotiv.model.User
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import java.security.MessageDigest
import java.util.*

class AccountAuthActivity : ComponentActivity() {
    private fun checkLogin(auth : String, callback: ClientCallback) {
        val client = UploadClient.getClient(applicationContext.getString(R.string.server))
        client.login(auth).enqueue(callback)
    }

    private fun register(username : String, password : String, callback: ClientCallback) {
        val client = UploadClient.getClient(applicationContext.getString(R.string.server))
        client.register(User(username, password)).enqueue(callback)
    }

    private fun add(username : String, password : String, register : Boolean, response : AccountAuthenticatorResponse?) {
        val base = Base64.getUrlEncoder()
        val md = MessageDigest.getInstance("SHA-256")
        val passwordDigest = base.encodeToString(md.digest(password.toByteArray()))
        val credentials = "$username:$passwordDigest"
        val auth = "Basic " + base.encodeToString(credentials.toByteArray())

        val callback = ClientCallback {
            if (it.code() == 200) {
                val am = AccountManager.get(applicationContext)
                val accountType = applicationContext.getString(R.string.accountType)
                Account(username, accountType).also { account ->
                    am.addAccountExplicitly(account, passwordDigest, null)
                }
                val bundle = Bundle()
                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, username)
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
                response?.onResult(bundle)
                finish()
            }
            else {
                Toast.makeText(applicationContext, "Identifiants incorrectes", Toast.LENGTH_SHORT).show()
                response?.onError(it.code(), "Cannot add account")
            }
        }
        if (register) {
            register(username, passwordDigest, callback)
        }
        else {
            checkLogin(auth, callback)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val register = intent.extras?.getBoolean(applicationContext.getString(R.string.register_option))
            ?: true
        val response : AccountAuthenticatorResponse? = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        response?.onRequestContinued()
        setContent {
            ActivMotivTheme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var username by rememberSaveable { mutableStateOf("") }
                        var password by rememberSaveable { mutableStateOf("") }
                        Text(
                            fontSize = 35.sp,
                            text = if (register) "Création de compte" else "Connexion"
                        )
                        TextField(
                            label = {Text("ID")},
                            value = username,
                            onValueChange = { username = it }
                        )
                        TextField(
                            label = {Text("Mot de passe")},
                            value = password,
                            onValueChange = { password = it },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Button(
                            onClick = {
                                add(username, password, register, response)
                            }
                        ) {
                            Text(if (register) "Créer" else "Se connecter")
                        }
                    }
                }
            }
        }
    }
}