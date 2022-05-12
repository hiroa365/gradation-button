package io.github.hiroa365.gradation_button_sample.screen.settings


data class SettingsScreenState(
    val cellsOptions: List<CellsNumber> = listOf(
        CellsNumber9,
        CellsNumber16,
        CellsNumber25
    ),
    val selectedOption: CellsNumber
)

sealed class CellsNumber(val height: Int, val width: Int) {
    override fun toString(): String = "$height × $width"

    companion object {
        fun find(height: Int, width: Int): CellsNumber {
            return when (height to width) {
                3 to 3 -> CellsNumber9
                4 to 4 -> CellsNumber16
                5 to 5 -> CellsNumber25
                else -> CellsNumber16
            }
        }
    }
}

object CellsNumber9 : CellsNumber(3, 3)
object CellsNumber16 : CellsNumber(4, 4)
object CellsNumber25 : CellsNumber(5, 5)
