package com.example.birdfeeder.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdfeeder.R
import com.example.birdfeeder.data.Meal
import com.example.birdfeeder.data.getMealManagerInstance
import com.example.birdfeeder.data.getRawFoodDataHandlerInstance
import com.example.birdfeeder.ui.theme.BirdFeederTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BirdFeederTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParentFeeder()
                }
            }
        }
        loadAndProcessFoodData()
    }

    private fun loadAndProcessFoodData() {
        val mealList = loadFoodDataFromRawResource()
        processFoodData(mealList)
    }

    private fun loadFoodDataFromRawResource(): MutableList<Meal> {
        val rawFoodDataHandler = getRawFoodDataHandlerInstance();
        rawFoodDataHandler.readFoodDataFromRawResource(resources.openRawResource(R.raw.feeder_feed));
        return rawFoodDataHandler.getLoadedMeals()
    }

    private fun processFoodData(mealList: MutableList<Meal>) {
        getMealManagerInstance().parseLoadedMeals(mealList)
    }

    @Composable
    fun ParentFeeder() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            FeederLogo()
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
            ) {
                    QuickFeeder()
                    MealPlanner()
                    ConfigureFoodData()
            }
        }

    }

    @Composable
    fun FeederLogo() {
        Image(
            painter = painterResource(R.drawable.bird_feeder_logo),
            contentDescription = null, // decorative
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun QuickFeeder() {
        Card(
            onClick = {quickFeed()},
            modifier = Modifier.size(width = 400.dp, height = 150.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ){
            Column() {

                Image(
                    painter = painterResource(R.drawable.salad_bowl),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = "Quick feed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )

                Text(
                    text = "Random one-off meal suggestion",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MealPlanner() {
        Card(
            onClick = {planMeal()},
            modifier = Modifier.size(width = 400.dp, height = 150.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ){
            Column() {

                Image(
                    painter = painterResource(R.drawable.avocado),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = "Meal planner",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )

                Text(
                    text = "Plan an entire day's meal",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfigureFoodData() {
        Card(
            onClick = {configureData()},
            modifier = Modifier.size(width = 400.dp, height = 150.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ){
            Column() {

                Image(
                    painter = painterResource(R.drawable.burger),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = "Deconstructed food",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )

                Text(
                    text = "Configure food data",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp, 0.dp)
                )
            }
        }
    }

    private fun quickFeed() {
        val intent = Intent(this, QuickFeeder::class.java)
        startActivity(intent)
    }

    private fun planMeal() {
        val intent = Intent(this, MealPlanner::class.java)
        startActivity(intent)
    }

    private fun configureData() {
        val intent = Intent(this, ConfigureData::class.java)
        startActivity(intent)
    }

}