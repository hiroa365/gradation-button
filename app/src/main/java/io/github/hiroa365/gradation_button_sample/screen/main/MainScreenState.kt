package io.github.hiroa365.gradation_button_sample.screen.main

import io.github.hiroa365.gradation_button_sample.data.repository.ButtonStyle


data class MainScreenState(
    val toggle: Boolean = true,
    val buttonList: MutableList<ButtonStyle>,
    val cellWidth: Int,
    val cellHeight: Int,
    val openRetryDialog: Boolean,
)
