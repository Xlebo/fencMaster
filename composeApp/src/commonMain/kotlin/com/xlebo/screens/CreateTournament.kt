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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.xlebo.Platform
import com.xlebo.modifierUtils.backButton
import com.xlebo.modifierUtils.defaultButton
import com.xlebo.screens.Screen
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CreateTournament(
    navController: NavHostController,
    platform: Platform,
    viewModel: SharedViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
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
                value = uiState.name,
                onValueChange = { viewModel.setName(it) }
            )
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val path = platform.handleFileSelection()
                    viewModel.setParticipants(platform.handleParticipantsImport(path))
                    viewModel.setFilePath(path)
                }
            ) {
                Text("Nahraj ucastnikov")
            }
            Spacer(Modifier.width(15.dp))
            Text(uiState.filePath ?: "No file selected")
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
                        Screen.TournamentDetail
                    )
                }
            ) { Text("Založiť turnaj") }
        }
    }
}