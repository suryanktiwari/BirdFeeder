package com.example.birdfeeder.data

import androidx.compose.ui.graphics.Color
import com.example.birdfeeder.R
import kotlin.random.Random

class MealConstants {
    enum class MealTypes(val value: String) {
        BREAKFAST("breakfast"),
        LUNCH("lunch"),
        DINNER("dinner"),
        SNACK("snack"),
    }

    companion object {
        const val COMMA_DELIMITER: String = ",";
        const val SEMI_COLON_DELIMITER: String = ";";

        val MIDNIGHT_SNACK_HOUR_RANGE = 0..5;
        val BREAKFAST_HOUR_RANGE = 6..10;
        val LUNCH_HOUR_RANGE = 11..15;
        val SNACK_HOUR_RANGE = 16..18;
        val DINNER_HOUR_RANGE = 18..23;

        val MEAL_BACKGROUNDS = mapOf(
            MealTypes.BREAKFAST to R.drawable.breakfast,
            MealTypes.LUNCH to R.drawable.lunch,
            MealTypes.DINNER to R.drawable.dinner,
            MealTypes.SNACK to R.drawable.snacks,
        )

        val MEAL_PREFIX_DIALOGUES = listOf<String>(
            "You could have some",
            "How about having",
            "A good candidate is",
            "Sometimes I like having",
            "How about some",
            "You're not ready for",
            "Mouthwatering",
            "You can never go wrong with",
            "Remember when you had",
            "I suggest having",
        );

        val MEAL_ACCOMPANIMENT_PREFIX_DIALOGUES = listOf<String>(
            "with",
            "and",
            "could be",
            "accompanied by",
        )

        val MEAL_VARIATIONS_PREFIX_DIALOGUES = listOf<String>(
            "Could be cooked like",
            "Potential matches",
            "Variations",
            "Varied as",
            "Like",
        )

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

        fun capitalizeFirstLetterOfString(str: String): String {
            return str.substring(0, 1).uppercase() + str.substring(1).lowercase()
        }
    }
}