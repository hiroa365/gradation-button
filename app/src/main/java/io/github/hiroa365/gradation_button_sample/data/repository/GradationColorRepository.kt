package io.github.hiroa365.gradation_button_sample.data.repository

import androidx.compose.ui.graphics.Color
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface GradationColorRepository {

    /**
     * Brush.colors に設定するカラーパターンの取得
     */
    val brushColors: List<Color>

    /**
     * グラデーションカラーのリストをランダム取得
     */
    operator fun invoke(): List<Color>
}

class GradationColorRepositoryImpl @Inject constructor() : GradationColorRepository {

    override val brushColors: List<Color>
        get() = listOf(
            Color(0xFFFF0000),  //赤
            Color(0xFFFF4000),  //朱色
            Color(0xFFFF8000),  //オレンジ色
            Color(0xFFFFBF00),  //ゴールデンイエロー
            Color(0xFFFFFF00),  //黄色
            Color(0xFFBFFF00),  //明るい黄緑色
            Color(0xFF80FF00),  //黄緑色
            Color(0xFF40FF00),  //リーフグリーン
            Color(0xFF00FF00),  //緑
            Color(0xFF00FF40),  //コバルトグリーン
            Color(0xFF00FF80),  //エメラルドグリーン
            Color(0xFF00FFBF),  //青緑色
            Color(0xFF00FFFF),  //シアン
            Color(0xFF00BFFF),  //セルリアンブルー
            Color(0xFF0080FF),  //青
            Color(0xFF0040FF),  //コバルトブルー
            Color(0xFF0000FF),  //群青色
            Color(0xFF4000FF),  //ヒヤシンス
            Color(0xFF8000FF),  //バイオレット
            Color(0xFFBF00FF),  //紫
            Color(0xFFFF00FF),  //マゼンタ
            Color(0xFFFF00BF),  //赤紫色
            Color(0xFFFF0080),  //ルビーレッド
            Color(0xFFFF0040),  //紅色
        )

    override operator fun invoke(): List<Color> {
        //ベースカラーを取得
        val baseColor: Color = brushColors.random()
        //パラーパターンを取得
        val pattern = when ((0..6).random()) {
            0 -> whiteColor(baseColor)
            1 -> toneColor(baseColor)
            2 -> rightSkipTwoColor(baseColor)
            3 -> rightSkipOneColor(baseColor)
            4 -> rightThreeColor(baseColor)
            5 -> rightTwoColor(baseColor)
            else -> rightOneColor(baseColor)
        }
        //透過度を取得
        val alpha = alphaList.random()
        //透過度お設定をして返却
        return pattern.map { it.copy(alpha = it.alpha * alpha) }
    }


    private val alphaList = listOf(
//        1.0f,   //100%
//        0.9f,   //90%
        0.8f,   //80%
        0.7f,   //70%
        0.6f,   //60%
        0.5f,   //50%
        0.4f,   //40%
        0.3f,   //30%
//        0.2f,   //20%
//        0.1f,   //10%
//        0.0f,   //0%
    )


    /**
     * 白
     */
    private fun whiteColor(
        baseColor: Color,
    ): List<Color> {
        return listOf(baseColor, Color.White)
    }

    /**
     * トーン違い
     */
    private fun toneColor(
        baseColor: Color,
    ): List<Color> {
        return listOf(
            baseColor,
            baseColor.copy(alpha = 0.5f)
        )
    }

    /**
     * ２つ飛ばし
     */
    private fun rightSkipTwoColor(
        baseColor: Color,
        skipColors: Int = 6
    ): List<Color> {
        //リストを2つ連結して疑似的に循環リストを作る
        val baseBrushColors = brushColors + brushColors
        //ベースカラーのindexを取得
        val index = baseBrushColors.indexOfFirst { it == baseColor }
        //１つ飛ばしの色を取得する
        return listOf(baseBrushColors[index], baseBrushColors[index + skipColors])
    }

    /**
     * １つ飛ばし
     */
    private fun rightSkipOneColor(
        baseColor: Color,
        skipColors: Int = 4
    ): List<Color> {
        //リストを2つ連結して疑似的に循環リストを作る
        val baseBrushColors = brushColors + brushColors
        //ベースカラーのindexを取得
        val index = baseBrushColors.indexOfFirst { it == baseColor }
        //１つ飛ばしの色を取得する
        return listOf(baseBrushColors[index], baseBrushColors[index + skipColors])
    }

    /**
     * 右隣３色
     */
    private fun rightThreeColor(
        baseColor: Color,
        pickupColors: Int = 7
    ): List<Color> {
        //リストを2つ連結して疑似的に循環リストを作る
        val baseBrushColors = brushColors + brushColors
        //ベースカラーのindexを取得
        val index = baseBrushColors.indexOfFirst { it == baseColor }
        //取り出す色の数の分だけ抽出する
        return baseBrushColors.subList(index, index + pickupColors)
    }

    /**
     * 右隣２色
     */
    private fun rightTwoColor(
        baseColor: Color,
        pickupColors: Int = 5
    ): List<Color> {
        //リストを2つ連結して疑似的に循環リストを作る
        val baseBrushColors = brushColors + brushColors
        //ベースカラーのindexを取得
        val index = baseBrushColors.indexOfFirst { it == baseColor }
        //取り出す色の数の分だけ抽出する
        return baseBrushColors.subList(index, index + pickupColors)
    }

    /**
     * 右隣１色
     */
    private fun rightOneColor(
        baseColor: Color,
        pickupColors: Int = 3
    ): List<Color> {
        //リストを2つ連結して疑似的に循環リストを作る
        val baseBrushColors = brushColors + brushColors
        //ベースカラーのindexを取得
        val index = baseBrushColors.indexOfFirst { it == baseColor }
        //取り出す色の数の分だけ抽出する
        return baseBrushColors.subList(index, index + pickupColors)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object GradationColorRepositoryModule {
    @Provides
    @Singleton
    fun provideGradationColorRepository(): GradationColorRepository {
        return GradationColorRepositoryImpl()
    }
}