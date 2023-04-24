package eu.euromov.activmotiv

import android.accounts.Account
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(onGetInfo: () -> Unit, account : Account, onDisconnect: () -> Unit) {
    TitledPage(
        stringResource(id = R.string.profile),
        onGetInfo
    ) {
        var showConfirm by remember { mutableStateOf(false) }
        if (showConfirm) {
            AlertDialog (
                text = {
                    Text("Voulez vous vraiment vous déconnecter?")
                },
                confirmButton = {
                    Button(onClick = onDisconnect) {
                        Text("Confirmer")
                    }
                },
                dismissButton = {
                    Button(onClick = {showConfirm = false}) {
                        Text("Annuler")
                    }
                },
                onDismissRequest = {showConfirm = false}
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                account.name,
                {},
                label = { Text(stringResource(R.string.id)) },
                readOnly = true
            )
            TextField(
                "Mot de passe",
                {},
                label = { Text(stringResource(R.string.password)) },
                readOnly = true
            )
            Button(
                onClick = { showConfirm = true }
            ) {
                Text(stringResource(R.string.disconnect))
            }
        }
    }
}