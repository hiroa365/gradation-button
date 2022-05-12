package io.github.hiroa365.gradation_button_sample.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateButtonSetup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val createButtonSetup: CreateButtonSetup,
    private val settingRepository: SettingsRepository,
) : ViewModel() {

//    private var settings = settingRepository.get()

    private val initValue: SettingsScreenState = SettingsScreenState(
        selectedOption = CellsNumber.find(
            width = settingRepository.get().cellWidth,
            height = settingRepository.get().cellHeight
        )
    )

    private val _state = MutableStateFlow(initValue)
    val state = _state.asStateFlow()


    fun onOptionSelected(value: CellsNumber) {
        viewModelScope.launch {
            _state.value = _state.value.copy(selectedOption = value)
            //データ更新
            settingRepository.setCells(height = value.height, width = value.width)
            //ボタン情報を再作成
            createButtonSetup()
        }
    }
}

