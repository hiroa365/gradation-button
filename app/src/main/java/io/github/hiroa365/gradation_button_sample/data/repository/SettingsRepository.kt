package io.github.hiroa365.gradation_button_sample.data.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsRepository {

//    suspend fun get(): Map<String, String>

//    suspend fun set(key: String, value: String)

    suspend fun setCells(width: Int, height: Int)

    fun get(): Settings

    suspend fun set(value: Settings)
}

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

//    private val settingsMap = mutableMapOf<String, String>(
//        Pair("CellWidth", "5"),
//        Pair("CellHeight", "5"),
//    )
//
//    override suspend fun get(): Map<String, String> {
//        return settingsMap
//    }
//
//    override suspend fun setCells(width: Int, height: Int) {
//        withContext(Dispatchers.IO) {
//            set("CellWidth", width.toString())
//            set("CellHeight", height.toString())
//        }
//    }
//
//    override suspend fun set(key: String, value: String) {
//        withContext(Dispatchers.IO) {
//            if (settingsMap.contains(key)) {
//                settingsMap[key] = value
//            }
//        }
//    }

    private var settings = Settings()

    override suspend fun setCells(width: Int, height: Int) {
        withContext(Dispatchers.IO) {
            settings = settings.copy(cellWidth = width, cellHeight = height)
        }
    }

    override fun get(): Settings {
        return settings
    }

    override suspend fun set(value: Settings) {
        withContext(Dispatchers.IO) {
            settings = value
        }
    }

}

data class Settings(
    /**
     * セルの横の数
     */
    val cellWidth: Int = 5,
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
