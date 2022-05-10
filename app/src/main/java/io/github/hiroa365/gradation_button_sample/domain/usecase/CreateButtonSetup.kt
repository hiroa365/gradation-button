package io.github.hiroa365.gradation_button_sample.domain.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.hiroa365.gradation_button_sample.data.repository.ButtonSetupRepository
import io.github.hiroa365.gradation_button_sample.data.repository.ButtonStyle
import io.github.hiroa365.gradation_button_sample.data.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

interface CreateButtonSetup {
    suspend operator fun invoke(): MutableList<ButtonStyle>
}

class CreateButtonSetupImpl @Inject constructor(
    private val buttonSetupRepository: ButtonSetupRepository,
    private val createBrushUseCase: CreateBrushUseCase,
    private val settingsRepository: SettingsRepository
) : CreateButtonSetup {


    override suspend fun invoke(): MutableList<ButtonStyle> {
        /**
         * 設定データからセル番号を取得
         */
        val cellNumber: Int = settingsRepository.get().cellNumber

        /**
         * セル数のボタンデータを生成してシャッフルする
         */
        val buttons = MutableList<ButtonStyle>(cellNumber) {
            ButtonStyle(
                brush = createBrushUseCase(),
                counter = it + 1,
            )
        }.apply { shuffle() }

        /**
         * 作成したデータをDBに保存
         */
        buttonSetupRepository.update(buttons)

        return buttons
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ButtonSetupUseCaseModule {
    @Provides
    @Singleton
    fun provideButtonSetupUseCase(
        buttonSetupRepository: ButtonSetupRepository,
        createBrushUseCase: CreateBrushUseCase,
        settingsRepository: SettingsRepository,
    ): CreateButtonSetup {
        return CreateButtonSetupImpl(
            buttonSetupRepository = buttonSetupRepository,
            createBrushUseCase = createBrushUseCase,
            settingsRepository = settingsRepository,
        )
    }
}