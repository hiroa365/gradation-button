package io.github.hiroa365.gradation_button_sample.data.repository

import androidx.compose.runtime.mutableStateOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsRepository {

    fun get(): Settings

    fun setCells(width: Int, height: Int)
}

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

    private val settings = mutableStateOf(Settings())

    override fun get(): Settings {
        return settings.value.copy()
    }

    override fun setCells(width: Int, height: Int) {
        settings.value = settings.value.copy(cellWidth = width, cellHeight = height)
    }


}

data class Settings(
    /**
     * セルの横の数
     */
    val cellWidth: Int = 4,
    /**
     * セルの縦の数
     */
    val cellHeight: Int = 5,
) {
    /**
     * セルの合計数
     */
    val cellNumber
        get() = cellWidth * cellHeight

}

@Module
@InstallIn(SingletonComponent::class)
object SettingsRepositoryModule {

    @Singleton
    @Provides
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }
}
