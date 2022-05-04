package io.github.hiroa365.gradation_button_sample.domain.usecase

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.hiroa365.gradation_button_sample.data.repository.GradationColorRepository
import javax.inject.Inject
import javax.inject.Singleton


/**
 * ボタン用の Brush を生成
 */
interface CreateBrushUseCase {
    operator fun invoke(): Brush
}


class CreateBrushUseCaseImpl @Inject constructor(
    /**
     * ボタン用のカラーを取得
     */
    private val gradationColorRepository: GradationColorRepository
) : CreateBrushUseCase {
    private val TAG = javaClass.simpleName

    override operator fun invoke(): Brush {
        //グラデーションカラーパターンを取得
        val colors = gradationColorRepository()
        //グラデーションBrush生成
        return when ((0..2).random()) {
            0 -> linearGradient(colors)
            1 -> horizontalGradient(colors)
            else -> verticalGradient(colors)
        }
    }

    /**
     * 斜め方向のグラデーション
     */
    private fun linearGradient(colors: List<Color>): Brush {
        //グラデーションの方向
        val (start, end) = listOf(
            listOf(
                Offset.Zero,
                Offset.Infinite
            ),
            listOf(
                Offset.Infinite,
                Offset.Zero
            ),
            listOf(
                Offset(100.0f, Float.POSITIVE_INFINITY),
                Offset(Float.POSITIVE_INFINITY, 100.0f)
            ),
            listOf(
                Offset(Float.POSITIVE_INFINITY, 0.0f),
                Offset(0.0f, Float.POSITIVE_INFINITY)
            ),
        ).random()

        Log.i(TAG, "create LinearGradient colors=$colors, start=$start end=$end")
        return Brush.linearGradient(colors = colors, start = start, end = end)
    }

    /**
     * 水平方向のグラデーション
     */
    private fun horizontalGradient(colors: List<Color>): Brush {
        //グラデーションの方向
        val (start, end) = listOf(
            listOf(0.0f, Float.POSITIVE_INFINITY),
            listOf(Float.POSITIVE_INFINITY, 0.0f),
        ).random()

        Log.i(TAG, "create HorizontalGradient colors=$colors, start=$start end=$end")
        return Brush.horizontalGradient(
            colors = colors,
            startX = start,
            endX = end,
            tileMode = TileMode.Clamp,
        )
    }

    /**
     * 縦方向のグラデーション
     */
    private fun verticalGradient(colors: List<Color>): Brush {
        //グラデーションの方向
        val (start, end) = listOf(
            listOf(0.0f, Float.POSITIVE_INFINITY),
            listOf(Float.POSITIVE_INFINITY, 0.0f),
        ).random()

        Log.i(TAG, "create VerticalGradient colors=$colors, startY=$start endY=$end")
        return Brush.verticalGradient(
            colors = colors,
            startY = start,
            endY = end,
        )
    }
}


@Module
@InstallIn(SingletonComponent::class)
object CreateButtonBrushUseCaseModule {
    @Provides
    @Singleton
    fun provideCreateButtonBrushUseCase(
        gradationColorRepository: GradationColorRepository
    ): CreateBrushUseCase {
        return CreateBrushUseCaseImpl(gradationColorRepository)
    }
}
