@file:OptIn(ExperimentalFoundationApi::class)

package io.github.hiroa365.gradation_button_sample.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateBrushUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * statefull
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    MainScreen(
        buttonList = state.buttonList,
        onPush = { viewModel.push(it) }
    )
}

/**
 * state less
 */
@Composable
fun MainScreen(
    buttonList: List<ButtonStyle>,
    onPush: (Long) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val screenHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }

        LazyVerticalGrid(
            cells = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            items(items = buttonList) { style ->
                MultiButton(
                    style = style,
                    height = screenHeight / 10,
                    onPush = onPush,
                )
            }
        }
    }
}

@Composable
fun MultiButton(
    style: ButtonStyle,
    height: Dp,
    onPush: (Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .height(height = height)
//            .fillMaxSize()
            .background(brush = style.brush)
            .noRippleClickable { onPush(style.id) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = style.counter.toString(),
            fontSize = 50.sp,
            color = Color.Black
        )
    }

}


/**
 * Ripple無し clickable
 */
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    indication: Indication? = null,
    onClick: () -> Unit
) = composed {
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick,
        role = role,
        indication = indication,
        interactionSource = remember { MutableInteractionSource() }
    )
}

@Preview
@Composable
fun MainScreenPreviw() {
    MainScreen(
        buttonList = listOf(
            ButtonStyle(
                brush = Brush.radialGradient(
                    listOf(Color.Red, Color.Yellow, Color.White)
                ),
                counter = 0,
            )
        ),
        onPush = { }
    )
}


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createBrushUseCase: CreateBrushUseCase
) : ViewModel() {
    private val initValue = MainScreenState(
        buttonList = MutableList<ButtonStyle>(50) {
            ButtonStyle(
                brush = createBrushUseCase(),
                counter = it + 1,
            )
        }.apply { shuffle() }
    )

    /**
     * StateはStateFlowで保持
     */
    private val _state = MutableStateFlow(initValue)
    val state = _state.asStateFlow()

    /**
     * ボタン押下
     */
    fun push(id: Long) {
        viewModelScope.launch {
            //ボタンインデックスの抽出
            val index = _state.value.buttonList.indexOfFirst { it.id == id }
            //古いスタイルの取得
            val oldStyle = _state.value.buttonList[index]

            //最小ボタンのチェック
            val minStyle = _state.value.buttonList.minByOrNull { it.counter }
            if (oldStyle.counter != minStyle?.counter) return@launch

            //最大カウンターの取得
            val style = _state.value.buttonList.maxByOrNull { it.counter }
            val maxCounter = style?.counter ?: oldStyle.counter

            //スタイルの更新
            _state.value.buttonList[index] =
                oldStyle.copy(brush = createBrushUseCase(), counter = maxCounter + 1)

            _state.value = _state.value.copy(
                toggle = !_state.value.toggle,
            )
        }
    }

    /**
     * ボタンカラー変更
     */

    /**
     * 音を出す
     */

    /**
     * 消える
     */

}

data class MainScreenState(
    val toggle: Boolean = true,
    val buttonList: MutableList<ButtonStyle>,
)

data class ButtonStyle(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val counter: Int,
    val brush: Brush,
)