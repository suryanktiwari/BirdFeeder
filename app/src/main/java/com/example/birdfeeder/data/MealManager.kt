package com.example.birdfeeder.data

class Meal(
    val name: String,
    val type: List<String>,
    val variations: List<String>,
    val accompaniment: List<String>
) {
    override fun toString(): String {
        return "Meal(name=$name, type=$type, variations=$variations, accompaniment=$accompaniment)"
    }
}

private val mealManagerInstance: MealManager = MealManager()

fun getMealManagerInstance(): MealManager {
    return mealManagerInstance
}

class MealManager {

    private val mealsByType: HashMap<String, MutableList<Meal>> = HashMap()

    fun getMealsByType(type: String): MutableList<Meal> {
        return if (type in mealsByType) {
            mealsByType[type]!!;
        } else {
            mutableListOf()
        }
    }

    fun getMealsByAllType(): HashMap<String, MutableList<Meal>> {
        return mealsByType;
    }

    fun parseLoadedMeals(loadedMeals: MutableList<Meal>) {
        for (meal in loadedMeals) {
                meal.type.forEach { type ->
                if (type in mealsByType) {
                    mealsByType[type]!!.add(meal)
                } else {
                    mealsByType[type] = mutableListOf(meal)
                }
            }
        }

        println(mealsByType)
    }
}