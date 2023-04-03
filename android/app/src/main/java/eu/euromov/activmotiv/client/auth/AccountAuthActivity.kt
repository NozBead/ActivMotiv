package eu.euromov.activmotiv.client.auth

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class AccountAuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val register = intent.extras?.getBoolean(applicationContext.getString(R.string.register_option)) ?: true
        val response : AccountAuthenticatorResponse? = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        val authenticator = ClientAuthenticator(applicationContext)
        response?.onRequestContinued()
        setContent {
            ActivMotivTheme {
                val action = if (register) authenticator::register else authenticator::checkLogin
                var loading by rememberSaveable { mutableStateOf(false) }
                Form(loading, register) { username, password ->
                    val request = lifecycleScope.async(Dispatchers.IO) {
                        try {
                            return@async action(username, password)
                        }
                        catch (e: Exception) {
                            return@async null
                        }
                    }

                    lifecycleScope.launch(Dispatchers.Main) {
                        loading = true
                        val result = request.await()
                        loading = false
                        if (result == null) {
                            Toast.makeText(
                                applicationContext,
                                "Impossible de joindre le serveur",
                                Toast.LENGTH_SHORT
                            ).show()
                            response?.onError(1, "Cannot add account")
                        }
                        else if(result.code() == 200) {
                            authenticator.pushAccount(response, username, password)
                            finish()
                        }
                        else {
                            Toast.makeText(
                                applicationContext,
                                "Identifiants incorrectes",
                                Toast.LENGTH_SHORT
                            ).show()
                            response?.onError(result.code(), "Cannot add account, incorrect ids")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(loading: Boolean, register: Boolean, onSubmit: (String, String) -> Unit) {
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
            if (!loading) {
                Button(
                    onClick = {
                        onSubmit(username, password)
                    }
                ) {
                    Text(if (register) "Créer" else "Se connecter")
                }
            }
            else {
                CircularProgressIndicator()
            }
        }
    }
}