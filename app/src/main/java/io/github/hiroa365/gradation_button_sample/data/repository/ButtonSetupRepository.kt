package io.github.hiroa365.gradation_button_sample.data.repository

import androidx.compose.ui.graphics.Brush
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface ButtonSetupRepository {

    fun get(): MutableList<ButtonStyle>

    suspend fun update(list: MutableList<ButtonStyle>)

    suspend fun updateAt(buttonStyle: ButtonStyle)

}

class ButtonSetupRepositoryImpl @Inject constructor() : ButtonSetupRepository {

    private var buttonSetup = ButtonSetup()


    override fun get(): MutableList<ButtonStyle> {
        return buttonSetup.buttonList
    }

    override suspend fun update(list: MutableList<ButtonStyle>) {
        withContext(Dispatchers.IO) {
            buttonSetup = buttonSetup.copy(buttonList = list)
        }
    }

    override suspend fun updateAt(buttonStyle: ButtonStyle) {
    }
}

data class ButtonSetup(
    /**
     * 表示するボタンテータのリスト
     */
    val buttonList: MutableList<ButtonStyle> = mutableListOf()
)

data class ButtonStyle(
    /**
     * ボタンID
     */
    val id: Long = UUID.randomUUID().mostSignificantBits,

    /**
     * カウンター
     */
    val counter: Int,

    /**
     * 色
     */
    val brush: Brush,
)

@Module
@InstallIn(SingletonComponent::class)
object ButtonSetupRepositoryModule {

    @Provides
    @Singleton
    fun provideButtonSetupRepository(): ButtonSetupRepository {
        return ButtonSetupRepositoryImpl()
    }
}