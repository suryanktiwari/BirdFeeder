package com.example.birdfeeder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdfeeder.data.Meal
import com.example.birdfeeder.data.MealConstants
import com.example.birdfeeder.data.MealConstants.Companion.randomColor
import com.example.birdfeeder.data.getMealManagerInstance
import com.example.birdfeeder.ui.theme.BirdFeederTheme

class ConfigureData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealManagerInstance = getMealManagerInstance();
        setContent {
            BirdFeederTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = randomColor()
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            // meal type text
                            Text(
                                text = "All meals by type",
                                fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                            )
                        }

                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            items(MealConstants.MealTypes.values()) { type ->
                                mealManagerInstance.getMealsByType(type.value)
                                    ?.let { MealTypeParentDisplay(type.value, it) }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MealTypeParentDisplay(mealType: String, meals: MutableList<Meal>) {
        Column {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = randomColor()
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                // meal type text
                Text(
                    text = capitalizeFirstLetterOfString(mealType),
                    fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                )

                // meals in the type
                meals.forEach { meal ->
                    MealRow(meal)
                }
            }
        }
    }

    @Composable
    fun MealRow(meal: Meal) {
        Card(
            modifier = Modifier
                .padding(all = 5.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Text(meal.name, fontSize = 18.sp, fontWeight = FontWeight.W500)

                if (meal.variations[0].isNotEmpty()) {
                    Row() {
                        Text("Variations:", fontSize = 10.sp)
                        Text(meal.variations.joinToString(), fontSize = 10.sp)
                    }

                }

                if (meal.accompaniment[0].isNotEmpty()) {
                    Row() {
                        Text("Accompaniments:", fontSize = 10.sp)
                        Text(meal.accompaniment.joinToString(), fontSize = 10.sp)
                    }
                }
            }
        }
    }

    private fun capitalizeFirstLetterOfString(str: String): String {
        return str.substring(0, 1).uppercase() + str.substring(1).lowercase()
    }
}