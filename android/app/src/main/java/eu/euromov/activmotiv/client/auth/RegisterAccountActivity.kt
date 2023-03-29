package eu.euromov.activmotiv.client.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
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
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme

class RegisterAccountActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                        TextField(
                            value = username,
                            onValueChange = { username = it }
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Button(
                            onClick = {
                                val am: AccountManager = AccountManager.get(applicationContext)
                                Account(username, applicationContext.getString(R.string.accountType)).also { account ->
                                    am.addAccountExplicitly(account, password, null)
                                }

                            }
                        ) {
                            Text("Register")
                        }
                    }
                }
            }
        }
    }
}