package com.example.birdfeeder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdfeeder.data.MealConstants
import com.example.birdfeeder.ui.theme.BirdFeederTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.birdfeeder.R
import com.example.birdfeeder.data.Meal
import com.example.birdfeeder.data.MealConstants.Companion.BREAKFAST_HOUR_RANGE
import com.example.birdfeeder.data.MealConstants.Companion.DINNER_HOUR_RANGE
import com.example.birdfeeder.data.MealConstants.Companion.LUNCH_HOUR_RANGE
import com.example.birdfeeder.data.MealConstants.Companion.MIDNIGHT_SNACK_HOUR_RANGE
import com.example.birdfeeder.data.MealConstants.Companion.QUICK_MEAL_ACCOMPANIMENT_PREFIX_DIALOGUES
import com.example.birdfeeder.data.MealConstants.Companion.QUICK_MEAL_PREFIX_DIALOGUES
import com.example.birdfeeder.data.MealConstants.Companion.QUICK_MEAL_VARIATIONS_PREFIX_DIALOGUES
import com.example.birdfeeder.data.MealConstants.Companion.SNACK_HOUR_RANGE
import com.example.birdfeeder.data.MealConstants.Companion.capitalizeFirstLetterOfString
import com.example.birdfeeder.data.getMealManagerInstance
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime


class QuickFeeder : ComponentActivity() {

    private var selectedMealType = MealConstants.MealTypes.values()[initializeMealTypeBasedOnCurrentTime()];
    private var mealsByType: MutableList<Meal> = mutableListOf();
    private var curMealIndex = 0;
    private var mealTitleState = MutableStateFlow("")
    private var mealVariantsState = MutableStateFlow("")
    private var mealAccompanimentsState = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BirdFeederTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column() {
                        Text(
                            text = "Quick meal suggestions",
                            fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                        )

                        QuickFeederParentDisplay()
                    }
                }
            }
        }
    }

    @Composable
    fun QuickFeederParentDisplay() {
        val mealHeaders = MealConstants.MealTypes.values()
        var selectedMealState by remember { mutableStateOf(initializeMealTypeBasedOnCurrentTime()) }
        TabRow(selectedTabIndex = selectedMealState) {
            mealHeaders.forEachIndexed { index, mealType ->
                Tab(
                    selected = selectedMealState == index,
                    onClick = { selectedMealState = index;  selectedMealType = mealHeaders[index]; },
                    text = { Text(text = capitalizeFirstLetterOfString(mealType.value), maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 12.sp) }
                )
            }
        }
        loadMealsForSelectedMealType()
        QuickFeedDisplay()
    }

    @Composable
    fun QuickFeedDisplay() {
        getQuickMeal();
        var areAllMealsFinished by remember { mutableStateOf(false) }

        Column (
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Card (
                modifier = Modifier.height(240.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {

                if (areAllMealsFinished) {
                    AllMealsFinishedDisplay()
                } else {
                    SuggestedMealDisplay()
                }

            }

            Button(
                modifier = Modifier.height(100.dp).fillMaxWidth().padding(20.dp),
                onClick = {
                    if (areAllMealsFinished) {
                        curMealIndex = 0;
                        areAllMealsFinished = false;
                        getQuickMeal()
                    }
                    if (curMealIndex == mealsByType.size) {
                        areAllMealsFinished = true;
                    } else {
                        getQuickMeal()
                    }
                }
            ) {
                Text("Feed")
            }
        }
    }

    @Composable
    private fun AllMealsFinishedDisplay() {
        Column(
            modifier = Modifier.padding(all = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.empty_data),
                contentDescription = null, // decorative
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(150.dp).fillMaxWidth()
            )

            Text("All meal category data displayed.")
        }
    }

    private fun initializeMealTypeBasedOnCurrentTime(): Int {
        val currentHour = LocalDateTime.now().hour
        val mealHeaders = MealConstants.MealTypes.values()
        return when {
            (currentHour in MIDNIGHT_SNACK_HOUR_RANGE)  -> mealHeaders.indexOf(MealConstants.MealTypes.SNACK)
            (currentHour in BREAKFAST_HOUR_RANGE)  -> mealHeaders.indexOf(MealConstants.MealTypes.BREAKFAST)
            (currentHour in LUNCH_HOUR_RANGE)  -> mealHeaders.indexOf(MealConstants.MealTypes.LUNCH)
            (currentHour in SNACK_HOUR_RANGE)  -> mealHeaders.indexOf(MealConstants.MealTypes.SNACK)
            (currentHour in DINNER_HOUR_RANGE)  -> mealHeaders.indexOf(MealConstants.MealTypes.DINNER)
            else ->
                mealHeaders.indexOf(MealConstants.MealTypes.SNACK)
        }
    }

    @Composable
    private fun SuggestedMealDisplay() {
        val mealTitleText by mealTitleState.collectAsState()
        val mealVariationText by mealVariantsState.collectAsState()
        val mealAccompanimentText by mealAccompanimentsState.collectAsState()
        val accompanimentScrollState = rememberScrollState()
        val variationScrollState = rememberScrollState()

        Card(
            modifier = Modifier.padding(all = 5.dp).fillMaxWidth().fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(20.dp, 30.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                // Chatter text for meal name
                Text(
                    QUICK_MEAL_PREFIX_DIALOGUES.random(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Gray,
                )
                // Meal name
                Text(mealTitleText, fontSize = 18.sp, fontWeight = FontWeight.W500, modifier = Modifier.padding(20.dp, 0.dp))


                if (mealVariationText != "") {
                    // Chatter text for accompaniments
                    Text(
                        QUICK_MEAL_ACCOMPANIMENT_PREFIX_DIALOGUES.random(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                    )
                    Row (
                        modifier = Modifier.horizontalScroll(variationScrollState)
                    ) {
                        // Meal variations
                        Text(mealVariationText, fontSize = 13.sp, modifier = Modifier.padding(20.dp, 0.dp))
                    }
                }


                if (mealAccompanimentText != "") {
                    // Chatter text for meal variations
                    Text(
                        QUICK_MEAL_VARIATIONS_PREFIX_DIALOGUES.random(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                    )
                    Row (
                        modifier = Modifier.horizontalScroll(accompanimentScrollState)
                    ) {
                        // Meal Accompaniments
                        Text(mealAccompanimentText, fontSize = 13.sp, modifier = Modifier.padding(20.dp, 0.dp))
                    }
                }
            }
        }
    }

    /**
     * Called whenever meal type is changed from nav bar.
     * Loads meal by selected meal type from MealManager and shuffles them for randomized meals.
     */
    private fun loadMealsForSelectedMealType() {
        curMealIndex = 0
        mealsByType = getMealManagerInstance().getMealsByType(selectedMealType.value).shuffled().toMutableList()
    }

    /**
     * Reads the next meal object {@link curMealIndex} from {@link mealsByType} list
     * Increments {@link curMealIndex}.
     * Always called after checking that {@link curMealIndex} < {@link mealsByType}'s size
     * Sets {@link mealTitleState} with the meal name, forcing an UI update
     * If any variations or accompaniments are found then sets them in {@link mealVariantsState} or {@link mealAccompanimentsState}
     */
    private fun getQuickMeal(){
        val currentMeal = mealsByType[curMealIndex++]
        mealTitleState.value = currentMeal.name;

        if (currentMeal.variations[0].isNotEmpty()) {
            mealVariantsState.value = currentMeal.variations.joinToString()
        } else {
            mealVariantsState.value = ""
        }

        if (currentMeal.accompaniment[0].isNotEmpty()) {
            mealAccompanimentsState.value = currentMeal.accompaniment.joinToString()
        } else {
            mealAccompanimentsState.value = ""
        }
    }

}