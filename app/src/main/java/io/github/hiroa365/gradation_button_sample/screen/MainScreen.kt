@file:OptIn(ExperimentalFoundationApi::class)

package io.github.hiroa365.gradation_button_sample.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hiroa365.gradation_button_sample.R
import io.github.hiroa365.gradation_button_sample.data.repository.GradationColorRepositoryImpl
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateBrushUseCase
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateBrushUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private val TAG = "MainScreen"

/**
 * statefull
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()


    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MainScreen(
            buttonList = state.buttonList,
            onClickPush = { viewModel.onClickPushNumber(it) },
            onClickRetry = { viewModel.showRetryDialog() },
            cellWidth = state.cellWidth,
            cellHeight = state.cellHeight,
        )

        if (state.openRetryDialog) {
            RetryAlertDialog(
                onClickConfirm = {
                    viewModel.resetButtonNumber()
                    viewModel.hideRetryDialog()
                },
                onClickDismiss = {
                    viewModel.hideRetryDialog()
                },
            )
        }
    }
}


@Composable
fun RetryAlertDialog(
    onClickConfirm: () -> Unit = {},
    onClickDismiss: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.surface,
) {
    Log.i(TAG, "show AlertDialog")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable { onClickDismiss() },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .padding(30.dp),
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(backgroundColor),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(text = "最初からやり直しますか？")
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "いいえ",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.clickable { onClickDismiss() }
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                    Text(
                        text = "はい",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.clickable { onClickConfirm() }
                    )
                }
            }
        }
    }
}

/**
 * state less
 */
@Composable
fun MainScreen(
    buttonList: List<ButtonStyle>,
    onClickPush: (Long) -> Unit = {},
    onClickRetry: () -> Unit = {},
    cellWidth: Int,
    cellHeight: Int,
) {
    Scaffold(
        topBar = {
            MainTopBar(onClickRetry = onClickRetry)
        },
        content = {
            MainScreenContent(buttonList, onClickPush, cellWidth, cellHeight)
        },
        bottomBar = {
            //ボトムバーはコンテンツが裏に隠れるので止める
            //MainBottomBar()
        },
    )
}

/**
 * state less
 */
@Composable
fun MainScreenContent(
    buttonList: List<ButtonStyle>,
    onClickPush: (Long) -> Unit,
    cellWidth: Int,
    cellHeight: Int,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val screenHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }

        LazyVerticalGrid(
            cells = GridCells.Fixed(cellWidth),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            items(items = buttonList) { style ->
                MultiButton(
                    style = style,
                    height = screenHeight / cellHeight,
                    onClickPush = onClickPush,
                )
            }
        }
    }
}

@Composable
fun MultiButton(
    style: ButtonStyle,
    height: Dp,
    onClickPush: (Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .height(height = height)
//            .fillMaxSize()
            .background(brush = style.brush)
            .noRippleClickable { onClickPush(style.id) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = style.counter.toString(),
            fontSize = 50.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MainTopBar(
    onClickRetry: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
//            }
        },
        actions = {
//            //設定
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
//            }
            //リトライ
            IconButton(onClick = { onClickRetry() }) {
                Icon(painterResource(id = R.drawable.ic_baseline_replay_24), "Hint")
            }
//            //ヘルプ
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(painterResource(id = R.drawable.ic_baseline_help_24), "Hint")
//            }
        },
        backgroundColor = Color.White,
    )
}


//@Composable
//fun MainBottomBar() {
//    BottomAppBar(
////            modifier = Modifier.height(56.dp),
//        backgroundColor = Color.White,
//    ) {
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
//        }
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
//        }
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
//        }
//    }
//}

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

object Config {
    const val cellWidth: Int = 4
    const val cellHeight: Int = 5

    const val cellNumber = cellWidth * cellHeight
}

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createBrushUseCase: CreateBrushUseCase
) : ViewModel() {

    private val initButtonList
        get() = MutableList<ButtonStyle>(Config.cellNumber) {
            ButtonStyle(
                brush = createBrushUseCase(),
                counter = it + 1,
            )
        }.apply { shuffle() }

    private val initValue
        get() = MainScreenState(
            buttonList = initButtonList,
            cellHeight = Config.cellHeight,
            cellWidth = Config.cellWidth,
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
        _state.value = _state.value.copy(
            toggle = !_state.value.toggle,
            buttonList = initButtonList,
        )
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
    val cellWidth: Int,
    val cellHeight: Int,
    val openRetryDialog: Boolean,
)

data class ButtonStyle(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val counter: Int,
    val brush: Brush,
)


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        buttonList = MutableList<ButtonStyle>(Config.cellNumber) {
            ButtonStyle(
                brush = CreateBrushUseCaseImpl(GradationColorRepositoryImpl()).invoke(),
                counter = it + 1,
            )
        }.apply { shuffle() },
        cellWidth = 4,
        cellHeight = 5,
    )
}

@Preview
@Composable
fun DialogPreview() {
    RetryAlertDialog()
}

