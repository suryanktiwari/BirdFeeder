package com.example.birdfeeder.data

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
    }
}