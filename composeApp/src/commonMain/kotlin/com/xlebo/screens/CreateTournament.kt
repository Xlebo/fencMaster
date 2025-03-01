import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xlebo.Platform
import com.xlebo.modifierUtils.backButton
import com.xlebo.modifierUtils.defaultButton
import com.xlebo.screens.Screen


@Composable
fun CreateTournament(
    navController: NavHostController,
    platform: Platform,
) {
    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        var tournamentName by remember { mutableStateOf("Nový turnaj") }
        var file: String? by remember { mutableStateOf(null) }

        Text(
            text = "Nový Turnaj",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.padding(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Názov Turnaju", modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                value = tournamentName,
                onValueChange = { tournamentName = it }
            )
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    file = platform.handleFileSelection()
                }
            ) {
                Text("Nahraj ucastnikov")
            }
            Spacer(Modifier.width(15.dp))
            Text(file ?: "No file selected")
        }


        Row {
            Button(
                modifier = Modifier.backButton(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = { navController.popBackStack() }
            ) { Text("Back") }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.defaultButton(),
                onClick = {
                    navController.navigate(
                        Screen.TournamentDetail(tournamentName, file)
                    )
                }
            ) { Text("Založiť turnaj") }
        }
    }
}