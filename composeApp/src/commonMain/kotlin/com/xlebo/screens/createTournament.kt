import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import com.xlebo.FilePath
import com.xlebo.Platform
import com.xlebo.modifierUtils.backButton
import com.xlebo.modifierUtils.defaultButton
import com.xlebo.navigation.Screen
import com.xlebo.navigation.SimpleNavController


@Composable
fun CreateTournament(
    navController: SimpleNavController,
    platform: Platform,
) {
    Column {
        var tournamentName = remember { "Nový turnaj" }
        var file: FilePath? by remember { mutableStateOf(null) }

        Text(
            text = "Nový Turnaj",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.padding(20.dp))

        Row {
            Text("Názov Turnaju", modifier = Modifier.padding(10.dp))
            TextField(
                value = tournamentName,
                onValueChange = { tournamentName = it }
            )
        }

        Row {
            Button(
                onClick = {
                    file = platform.handleFileSelection()
                }
            ) {
                Text("Nahraj ucastnikov")
            }
            Text(file?.name ?: "No file selected")
        }


        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.backButton(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = { navController.navigateBack() }
            ) { Text("Back") }

            Spacer(modifier = Modifier.padding(20.dp))

            Button(
                modifier = Modifier.defaultButton(),
                onClick = { navController.navigateTo(Screen.TournamentDetail(file)) }
            ) { Text("Založiť turnaj") }
        }
    }
}