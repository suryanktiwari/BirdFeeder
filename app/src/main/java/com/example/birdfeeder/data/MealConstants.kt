package com.example.birdfeeder.data

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class MealConstants {
    enum class MealTypes(val value: String) {
        BREAKFAST("breakfast"),
        LUNCH("lunch"),
        DINNER("dinner"),
        SNACK("snack"),
        ANY("any"),
    }

    companion object {
        const val COMMA_DELIMITER: String = ",";
        const val SEMI_COLON_DELIMITER: String = ";";

        fun randomColor(): Color {
            val random = Random.Default
            // using these to make sure only light random colors are picked
            val fromColorRangeLimit = 0.5;
            val untilColorRangeLimit = 0.9;
            return Color(
                red = random.nextDouble(fromColorRangeLimit, untilColorRangeLimit).toFloat(),
                green = random.nextDouble(fromColorRangeLimit, untilColorRangeLimit).toFloat(),
                blue = random.nextDouble(fromColorRangeLimit, untilColorRangeLimit).toFloat(),
            )
        }
    }
}