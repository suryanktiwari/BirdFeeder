package com.example.birdfeeder.data

import com.example.birdfeeder.data.MealConstants.Companion.COMMA_DELIMITER
import com.example.birdfeeder.data.MealConstants.Companion.SEMI_COLON_DELIMITER
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

private val rawFoodResourceHandlerInstance: RawFoodResourceHandler = RawFoodResourceHandler()

fun getRawFoodDataHandlerInstance(): RawFoodResourceHandler {
    return rawFoodResourceHandlerInstance
}

class RawFoodResourceHandler {

    private var mealList: MutableList<Meal> = mutableListOf()

    /**
     * Used to parse headers in order from the raw CSV
     */
    enum class RawFoodResourceHeaderOrder(val value: Int) {
        ITEM(0),
        ITEM_TYPE(1),
        VARIATIONS(2),
        ACCOMPANIMENT(3),
    }

    fun getLoadedMeals(): MutableList<Meal> {
        return mealList;
    }

    /**
     * Reads food data from a InputStream, a passed raw resource
     * Creates Meal objects from each row present in the raw resource.
     */
    fun readFoodDataFromRawResource(rawResourceStream: InputStream) {
        // creating a buffered reader from an input stream reader from the raw resource stream passed
        val reader = BufferedReader(InputStreamReader(rawResourceStream, Charset.forName("UTF-8")))

        // need to read once to get the headers out of the file.
        val headers = reader.readLine().split(",")

        reader.readLines().forEach { row ->
            val cleanRow = row.replace("\"", "");

            //get a string array of all items in this line
            val rowItems = cleanRow.split(COMMA_DELIMITER)

            val newMeal = Meal(
                name = rowItems[RawFoodResourceHeaderOrder.ITEM.ordinal],
                type = rowItems[RawFoodResourceHeaderOrder.ITEM_TYPE.value].lowercase().split(SEMI_COLON_DELIMITER),
                variations = rowItems[RawFoodResourceHeaderOrder.VARIATIONS.ordinal].split(SEMI_COLON_DELIMITER),
                accompaniment = rowItems[RawFoodResourceHeaderOrder.ACCOMPANIMENT.ordinal].split(SEMI_COLON_DELIMITER)
            )
            mealList.add(newMeal)
        }
    }
}