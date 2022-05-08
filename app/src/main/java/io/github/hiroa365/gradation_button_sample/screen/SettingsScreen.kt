package io.github.hiroa365.gradation_button_sample.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import javax.inject.Inject

@Composable
fun SettingsScreen() {

}


class SettingsScreenViewModel @Inject constructor(
    private val settingRepository: SettingsRepository,
) : ViewModel() {

}


