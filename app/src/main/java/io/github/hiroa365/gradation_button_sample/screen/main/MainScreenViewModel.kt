package io.github.hiroa365.gradation_button_sample.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.data.repository.ButtonSetupRepository
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateBrushUseCase
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateButtonSetup
import io.github.hiroa365.gradation_button_sample.domain.usecase.SpeechNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createBrushUseCase: CreateBrushUseCase,
    private val createButtonSetup: CreateButtonSetup,
    private val speechNumber: SpeechNumber,
    private val settingRepository: SettingsRepository,
    private val buttonSetupRepository: ButtonSetupRepository,
) : ViewModel() {

    private val settings = settingRepository.get()

    /**
     * 初期値を作成
     */
    private val initValue
        get() = MainScreenState(
            buttonList = buttonSetupRepository.get(),
            cellHeight = settings.cellHeight,
            cellWidth = settings.cellWidth,
            openRetryDialog = false,
        )

    /**
     * StateはStateFlowで保持
     */
    private val _state = MutableStateFlow(initValue)
    val state = _state.asStateFlow()

    /**
     * ボタン押下
     */
    fun onClickPushNumber(id: Long) {
        viewModelScope.launch {
            //ボタンインデックスの抽出
            val index = _state.value.buttonList.indexOfFirst { it.id == id }
            //古いスタイルの取得
            val oldStyle = _state.value.buttonList[index]

            speechNumber(oldStyle.counter.toString())

            //最小ボタンのチェック
            val minStyle = _state.value.buttonList.minByOrNull { it.counter }
            if (oldStyle.counter != minStyle?.counter) return@launch

            //最大カウンターの取得
            val style = _state.value.buttonList.maxByOrNull { it.counter }
            val maxCounter = style?.counter ?: oldStyle.counter

            //スタイルの更新
            _state.value.buttonList[index] =
                oldStyle.copy(brush = createBrushUseCase(), counter = maxCounter + 1)

            _state.value = _state.value.copy(toggle = !_state.value.toggle)
        }
    }

    /**
     * リトライダイアログ表示
     */
    fun showRetryDialog() {
        _state.value = _state.value.copy(openRetryDialog = true)
    }

    /**
     * リトライダイアログ消去
     */
    fun hideRetryDialog() {
        _state.value = _state.value.copy(openRetryDialog = false)
    }

    /**
     * ボタン番号のリセット
     */
    fun resetButtonNumber() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                toggle = !_state.value.toggle,
                buttonList = createButtonSetup(),
                openRetryDialog = false,
            )
        }
    }
}
