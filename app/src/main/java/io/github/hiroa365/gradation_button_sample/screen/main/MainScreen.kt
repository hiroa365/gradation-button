@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package io.github.hiroa365.gradation_button_sample.screen.main

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.hiroa365.gradation_button_sample.R
import io.github.hiroa365.gradation_button_sample.data.repository.*
import io.github.hiroa365.gradation_button_sample.domain.usecase.CreateBrushUseCaseImpl

private val TAG = "MainScreen"

/**
 * state full
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    //ボタンが未初期化の場合は初期化する
    state.buttonList.ifEmpty { viewModel.resetButtonNumber() }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MainScreen(
            buttonList = state.buttonList,
            onClickPush = { viewModel.onClickPushNumber(it) },
            onClickRetry = { viewModel.showRetryDialog() },
            onClickSettings = { navigateToSettings() },
            cellWidth = state.cellWidth,
            cellHeight = state.cellHeight,
        )

        if (state.openRetryDialog) {
            RetryAlertDialog(
                onClickConfirm = {
                    viewModel.resetButtonNumber()
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
    onClickSettings: () -> Unit = {},
    cellWidth: Int,
    cellHeight: Int,
) {
    Scaffold(
        topBar = {
            MainTopBar(
                onClickRetry = onClickRetry,
                onClickSettings = onClickSettings
            )
        },
        content = {
            MainScreenContent(buttonList, onClickPush, cellWidth, cellHeight)
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
//        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
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
    onClickSettings: () -> Unit = {},
) {
    TopAppBar(
        title = {},
        navigationIcon = {
        },
        actions = {
            //設定
            IconButton(onClick = { onClickSettings() }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
            }
            //リトライ
            IconButton(onClick = { onClickRetry() }) {
                Icon(painterResource(id = R.drawable.ic_baseline_replay_24), "Hint")
            }
        },
        backgroundColor = Color.White,
    )
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
fun MainScreenPreview() {
    val settingRepository = SettingsRepositoryImpl()
    MainScreen(
        buttonList = MutableList<ButtonStyle>(settingRepository.get().cellNumber) {
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

