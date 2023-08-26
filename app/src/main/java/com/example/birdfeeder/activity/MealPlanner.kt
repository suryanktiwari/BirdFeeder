package com.example.birdfeeder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdfeeder.data.Meal
import com.example.birdfeeder.data.MealConstants
import com.example.birdfeeder.data.getMealManagerInstance
import com.example.birdfeeder.ui.theme.BirdFeederTheme


class MealPlanner : ComponentActivity() {
    private var mealSuggestionByType = mutableStateMapOf<String, Meal>()
    private val mealManagerInstance = getMealManagerInstance();

    private val plannedMeals = listOf(
        MealConstants.MealTypes.BREAKFAST,
        MealConstants.MealTypes.LUNCH,
        MealConstants.MealTypes.DINNER,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BirdFeederTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Meal planning",
                            fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                        )

                        MealPlannerParentDisplay()
                    }
                }
            }
        }
    }

    @Composable
    fun MealPlannerParentDisplay() {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(0.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(plannedMeals) { type ->
                MealPlannerForType(type.value)
            }

            item {
                Button(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(20.dp),
                    onClick = {
                        plannedMeals.forEach { type ->
                            loadNewMealForType(type.value)
                        }
                    }
                ) {
                    Text("Feed")
                }
            }
        }
    }

    @Composable
    fun MealPlannerForType(mealType: String) {
        loadNewMealForType(mealType)
        Column (
            modifier = Modifier
                .padding(10.dp, 5.dp)
                .fillMaxSize(),
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {

                // meal type text
                Text(
                    text = MealConstants.capitalizeFirstLetterOfString(mealType),
                    fontSize = 18.sp, fontWeight = FontWeight.W500, modifier = Modifier.padding(10.dp, 5.dp)
                )

                SuggestedMeal(mealType)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SuggestedMeal(mealType: String) {
        val accompanimentScrollState = rememberScrollState()
        val variationScrollState = rememberScrollState()
        val meal = mealSuggestionByType[mealType]!!

        Card(
            onClick = {loadNewMealForType(mealType)},
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .padding(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(5.dp, 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                // Chatter text for meal name
                Text(
                    MealConstants.MEAL_PREFIX_DIALOGUES.random(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Gray,
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
                // Meal name
                Text(meal.name, fontSize = 18.sp, fontWeight = FontWeight.W500, modifier = Modifier.padding(20.dp, 0.dp))


                if (meal.variations[0].isNotEmpty()) {
                    // Chatter text for meal variations
                    Text(
                        MealConstants.MEAL_VARIATIONS_PREFIX_DIALOGUES.random(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp, 0.dp),
                    )
                    Row (
                        modifier = Modifier.horizontalScroll(variationScrollState)
                    ) {
                        // Meal variations
                        Text(meal.variations.joinToString(), fontSize = 13.sp, modifier = Modifier.padding(20.dp, 0.dp))
                    }
                }

                if (meal.accompaniment[0].isNotEmpty()) {
                    // Chatter text for accompaniments
                    Text(
                        MealConstants.MEAL_ACCOMPANIMENT_PREFIX_DIALOGUES.random(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                    Row (
                        modifier = Modifier.horizontalScroll(accompanimentScrollState)
                    ) {
                        // Meal Accompaniments
                        Text(meal.accompaniment.joinToString(), fontSize = 13.sp, modifier = Modifier.padding(20.dp, 0.dp))
                    }
                }
            }
        }
    }

    private fun loadNewMealForType(mealType: String) {
        mealSuggestionByType[mealType] = mealManagerInstance.getMealsByType(mealType).random();
    }

}