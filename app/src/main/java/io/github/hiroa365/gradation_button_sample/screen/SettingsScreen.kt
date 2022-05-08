package io.github.hiroa365.gradation_button_sample.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                val radioOptions = listOf("3 × 3", "4 × 4", "5 × 5")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
                Column {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
//                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                    }
                                )
                                .padding(horizontal = 16.dp, vertical = 0.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                            Text(
                                text = text,
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


//            Row {
//                val radioOptions = listOf("3 × 3", "4 × 4", "5 × 5")
//                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
//                Column {
//                    radioOptions.forEach { text ->
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                                .selectable(
//                                    selected = (text == selectedOption),
//                                    onClick = {
//                                        onOptionSelected(text)
//                                    }
//                                )
//                                .padding(horizontal = 16.dp),
//                            horizontalArrangement = Arrangement.End,
//                            verticalAlignment = Alignment.CenterVertically,
//                        ) {
//                            RadioButton(
//                                selected = (text == selectedOption),
//                                onClick = { onOptionSelected(text) }
//                            )
//                            Text(
//                                text = text,
//                                style = MaterialTheme.typography.body1.merge(),
//                                modifier = Modifier.padding(start = 16.dp)
//                            )
//                        }
//                    }
//                }
//            }

//            Divider()
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
//                Text(
//                    modifier = Modifier.weight(1f),
//                    text = "音声",
//                )
//                Text(text = "")
//            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { navigateToMain() }) {
                    Text(text = "キャンセル")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = { navigateToMain() }) {
                    Text(text = "保存")
                }
            }
        }
    }
}


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingRepository: SettingsRepository,
) : ViewModel() {

    private val settings = settingRepository.get()

    private val initValue = SettingsScreenState(
        cellHeight = settings.cellHeight,
        cellWidth = settings.cellWidth,
    )

    private val _state = MutableStateFlow(initValue)
    val state = _state.asStateFlow()


}

data class SettingsScreenState(
    val cellHeight: Int,
    val cellWidth: Int,
)


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}

