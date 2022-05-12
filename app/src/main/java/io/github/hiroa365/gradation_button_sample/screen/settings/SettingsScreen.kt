package io.github.hiroa365.gradation_button_sample.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateButtonSetup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * state full
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    navigateToMain: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    SettingsScreen(
        radioOptions = state.cellsOptions,
        onOptionSelected = { viewModel.onOptionSelected(it) },
        selectedOption = state.selectedOption,
        onClickClose = { navigateToMain() },
    )
}

@Composable
fun SettingsScreen(
    radioOptions: List<CellsNumber>,
    onOptionSelected: (CellsNumber) -> Unit,
    selectedOption: CellsNumber,
    onClickClose: () -> Unit,
) {

    Scaffold(
        topBar = {},
        bottomBar = {},
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    text = "ボタンの数",
                )
                Column {
                    radioOptions.forEach { cellsnumber ->
                        Row(
                            Modifier
//                                .fillMaxWidth()
                                .selectable(
                                    selected = (cellsnumber == selectedOption),
                                    onClick = {
                                        onOptionSelected(cellsnumber)
                                    }
                                )
                                .padding(horizontal = 16.dp, vertical = 0.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (cellsnumber == selectedOption),
                                onClick = { onOptionSelected(cellsnumber) }
                            )
                            Text(
                                text = cellsnumber.toString(),
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

            }
            Divider()

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    text = "バージョン",
                )
                Text(text = "1.0.0")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = { onClickClose() }) {
                    Text(text = "完了")
                }
            }
        }
    }
}



@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}

