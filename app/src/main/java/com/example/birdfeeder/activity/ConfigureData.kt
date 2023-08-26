package com.example.birdfeeder.activity

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.birdfeeder.data.Meal
import com.example.birdfeeder.data.MealConstants
import com.example.birdfeeder.data.MealConstants.Companion.COMMA_DELIMITER
import com.example.birdfeeder.data.MealConstants.Companion.MEAL_BACKGROUNDS
import com.example.birdfeeder.data.MealConstants.Companion.SEMI_COLON_DELIMITER
import com.example.birdfeeder.data.MealConstants.Companion.capitalizeFirstLetterOfString
import com.example.birdfeeder.data.getMealManagerInstance
import com.example.birdfeeder.ui.theme.BirdFeederTheme

class ConfigureData : ComponentActivity() {
    // dictates whether or not a dialog box is to be shown to edit a particular meal
    private var showMealEditorDialog =  mutableStateOf(false)

    // contains state for meal object being edited
    lateinit var mealObjectToEdit: MutableState<Meal>;

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
                        // Header text
                        Text(
                            text = "All meals by type",
                            fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                        )

                        // Column containing all meal types and corresponding data
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            contentPadding = PaddingValues(10.dp),
                        ) {
                            items(MealConstants.MealTypes.values()) { type ->
                                MealTypeParentDisplay(type.value,
                                    mealManagerInstance.getMealsByType(type.value), MEAL_BACKGROUNDS[type]!!)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Contains all data for a certain meal type.
     * @param mealType String value for meal type for which data is displayed
     * @param meals Mutable list of all Meal Objects in the specified meal type
     * @param background Resource Image Integer to be displayed on the meal card.
     */
    @Composable
    fun MealTypeParentDisplay(mealType: String, meals: MutableList<Meal>, background: Int) {
        Column {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                // Fixed height image
                Image(
                    painter = painterResource(background),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(150.dp).fillMaxWidth()
                )

                // meal type text
                Text(
                    text = capitalizeFirstLetterOfString(mealType),
                    fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp)
                )

                // meals in the type
                meals.forEach { meal ->
                    // Making a mutable state of meal so it can be updated if user edits the meal.
                    val mealState =  remember { mutableStateOf(meal) }
                    MealRow(mealState)
                }


                // Meal editor dialog box
                if(showMealEditorDialog.value) {
                    MealEditorDialog(
                        meal = mealObjectToEdit.value,
                        setShowMealEditorDialog = {
                            showMealEditorDialog.value = it
                        },
                        setMeal = {
                            mealObjectToEdit.value = it
                        }
                    )
                }
            }
        }
    }

    /**
     * A row depicting one individual meal.
     * Each row has an onClick that triggers a dialog box for editing that particular row.
     * @param mealState MutableState containing a Meal Object which is to be displayed
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MealRow(mealState: MutableState<Meal>) {

        Card(
            modifier = Modifier.padding(all = 5.dp).fillMaxWidth(),
            onClick = {
                // show dialog box when true
                showMealEditorDialog.value = true;
                mealObjectToEdit = mealState;
            }
        ) {
            Column(
                modifier = Modifier.padding(all = 10.dp)
            ) {
                // Meal name text
                Text(mealState.value.name, fontSize = 18.sp, fontWeight = FontWeight.W500)

                // Meal variations if any
                if (mealState.value.variations[0].isNotEmpty()) {
                    Row() {
                        Text("Variations:", fontSize = 10.sp)
                        Text(mealState.value.variations.joinToString(), fontSize = 10.sp)
                    }
                }

                // Meal accompaniments if any
                if (mealState.value.accompaniment[0].isNotEmpty()) {
                    Row() {
                        Text("Accompaniments:", fontSize = 10.sp)
                        Text(mealState.value.accompaniment.joinToString(), fontSize = 10.sp)
                    }
                }
            }
        }
    }

    /**
     * Custom dialog box to enable user to edit a selected meal row.
     * @param meal Meal object being edited.
     * @param setShowMealEditorDialog State setter for {@link showMealEditorDialog}
     * @param setMeal State setter for Meal object currently being edited.
     */
    @Composable
    fun MealEditorDialog(
        meal: Meal,
        setShowMealEditorDialog: (Boolean) -> Unit,
        setMeal: (Meal) -> Unit
    ) {
        /**
         * Setting states for storing values for updated Meal Object
         */
        val mealName = remember { mutableStateOf(meal.name) }
        val mealNameError = remember { mutableStateOf("") }

        // mealType is displayed by joining all data with semicolon delimiter. Users are expected to enter input in same fashion.
        val mealType = remember { mutableStateOf(meal.type.joinToString(SEMI_COLON_DELIMITER)) }
        val mealTypeError = remember { mutableStateOf("") }

        // mealVariations is displayed by joining all data with semicolon delimiter. Users are expected to enter input in same fashion.
        val mealVariations = remember { mutableStateOf(meal.variations.joinToString(SEMI_COLON_DELIMITER)) }
        val mealVariationsError = remember { mutableStateOf("") }

        // mealAccompaniments is displayed by joining all data with semicolon delimiter. Users are expected to enter input in same fashion.
        val mealAccompaniments = remember { mutableStateOf(meal.variations.joinToString(SEMI_COLON_DELIMITER)) }
        val mealAccompanimentsError = remember { mutableStateOf("") }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Dialog(onDismissRequest = { setShowMealEditorDialog(false) }) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(all = 0.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(0.dp),
            ) {

                item {

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                // Dialog Box Title
                                MealEditorDialogTitle(setShowMealEditorDialog)

                                // Meal name is a separate property from other descriptors. Have put a separate editor for it.
                                Spacer(modifier = Modifier.height(20.dp))
                                MealTitleEditor(mealName, mealNameError)

                                // Meal descriptor editor for Meal Type property
                                Spacer(modifier = Modifier.height(20.dp))
                                MealDescriptorEditor(
                                    mealType,
                                    mealTypeError,
                                    "Meal Type",
                                    "Enter meal type"
                                )

                                // Meal descriptor editor for Meal Variations property
                                Spacer(modifier = Modifier.height(20.dp))
                                MealDescriptorEditor(
                                    mealVariations,
                                    mealVariationsError,
                                    "Meal Variations",
                                    "Enter meal variations"
                                )

                                // Meal descriptor editor for Meal Accompaniments property
                                Spacer(modifier = Modifier.height(20.dp))
                                MealDescriptorEditor(
                                    mealAccompaniments,
                                    mealAccompanimentsError,
                                    "Meal Accompaniments",
                                    "Enter meal accompaniments"
                                )

                                // Button to verify input data and update UI with new meal object
                                Box(modifier = Modifier.padding(40.dp, 20.dp, 40.dp, 0.dp)) {
                                    Button(
                                        onClick = {
                                            println("MealName is $mealName")

                                            // Validate that there is no comma in the input. Blanks are valid.
                                            if (mealName.value.isEmpty() || mealName.value.contains(COMMA_DELIMITER)) {
                                                mealNameError.value = "Field can not be empty or have commas"
                                            } else {
                                                mealNameError.value = "";
                                            }

                                            validateMealTypeValue(mealType.value, mealTypeError)

                                            validateMealDescriptorValue(mealVariations.value, mealVariationsError)
                                            validateMealDescriptorValue(mealAccompaniments.value, mealAccompanimentsError)

                                            // If there is any error, return.
                                            if (
                                                mealNameError.value != "" ||
                                                mealTypeError.value != "" ||
                                                mealVariationsError.value != "" ||
                                                mealAccompanimentsError.value != ""
                                            ) {
                                                return@Button
                                            }

                                            // Create new meal object with new values.
                                            val newMeal = Meal(
                                                name = mealName.value,
                                                type = mealType.value.split(SEMI_COLON_DELIMITER),
                                                variations = mealVariations.value.split(
                                                    SEMI_COLON_DELIMITER
                                                ),
                                                accompaniment = mealAccompaniments.value.split(
                                                    SEMI_COLON_DELIMITER
                                                )
                                            )

                                            // Setting setters to update UI.
                                            setMeal(newMeal)
                                            setShowMealEditorDialog(false)
                                        },
                                        shape = RoundedCornerShape(50.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                    ) {
                                        Text(text = "Update")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Title for the Meal Editor Dialog box
     */
    @Composable
    private fun MealEditorDialogTitle(
        setShowMealEditorDialog: (Boolean) -> Unit,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Set new meal details",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                tint = colorResource(android.R.color.darker_gray),
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .clickable { setShowMealEditorDialog(false) }
            )
        }
    }

    /**
     * Editor for the title of the meal.
     * @param mealName: State string that contains the meal name
     * @param mealNameError: State string that needs to be set for error in mealName.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MealTitleEditor(
        mealName: MutableState<String>,
        mealNameError: MutableState<String>,
    ) {
        Text("Meal name")

        // If there is an error in the descriptor then show the validation message
        if (mealNameError.value.isNotEmpty()) {
            Text(mealNameError.value, fontSize = 8.sp)
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        width = 2.dp,
                        color = colorResource(id = if (mealNameError.value.isEmpty()) R.color.holo_green_light else R.color.holo_red_dark)
                    ),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    tint = colorResource(android.R.color.holo_green_light),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            },
            placeholder = { Text(text = "Enter meal name") },
            value = mealName.value,
            onValueChange = {
                mealName.value = it
            })
    }

    /**
     * Editor for the descriptors of the meal.
     * @param mealDescriptor: State string that contains the meal descriptor
     * @param mealDescriptorTextEditorError: State string that needs to be set for error in meal descriptor
     * @param labelText Guiding text to be displayed above the TextInputBox.
     * @param editorPlaceHolder Guiding text to be displayed on the TextEditor when empty.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MealDescriptorEditor(
        mealDescriptor: MutableState<String>,
        mealDescriptorTextEditorError: MutableState<String>,
        labelText: String,
        editorPlaceHolder: String,
    ) {
        Text(labelText)
        Text("Values entered should be separated by ';'", fontSize = 8.sp)

        // If there is an error in the descriptor then show the validation message
        if (mealDescriptorTextEditorError.value.isNotEmpty()) {
            Text(mealDescriptorTextEditorError.value, fontSize = 8.sp)
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        width = 2.dp,
                        color = colorResource(id = if (mealDescriptorTextEditorError.value.isEmpty()) R.color.holo_green_light else R.color.holo_red_dark)
                    ),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    tint = colorResource(android.R.color.holo_green_light),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            },
            placeholder = { Text(text = editorPlaceHolder) },
            value = mealDescriptor.value,
            onValueChange = {
                mealDescriptor.value = it
            })
    }

    /**
     * Used for validating meal descriptors.
     * Following conditions are validated:
     *  1. Meal descriptor value should not be null.
     *  @param mealDescriptor String value of the meal descriptor being validated
     *  @param mealDescriptorEditorErrorState State setter to be set with error message if validation fails for mealDescriptor.
     */
    private fun validateMealDescriptorValue(
        mealDescriptor: String,
        mealDescriptorEditorErrorState: MutableState<String>
    ) {
        if (mealDescriptor.contains(COMMA_DELIMITER)) {
            mealDescriptorEditorErrorState.value = "Field can not be empty or have commas"
        } else {
            mealDescriptorEditorErrorState.value = "";
        }
    }

    /**
     * Validates the input meal type value.
     * Meal type must be a valid type, non empty and should not have commas
     * @param mealTypeValue String value of the mealType being validated
     *  @param mealTypeErrorEditorState State setter to be set with error message if validation fails for mealType.
     */
    private fun validateMealTypeValue(
        mealTypeValue: String,
        mealTypeErrorEditorState: MutableState<String>
    ) {
        if (isMealTypeValid(mealTypeValue)) {
            mealTypeErrorEditorState.value = "";
        } else {
            mealTypeErrorEditorState.value = "Meal type value is invalid. Only shown types can be entered."
        }
    }

    /**
     * Checks if the provided mealType is a valid meal type.
     * A valid meal type exists in {@link MealConstants.MealTypes}
     * @param mealType string value of meal type being checked for validity
     */
    private fun isMealTypeValid(mealType: String): Boolean {
        MealConstants.MealTypes.values().forEach { type ->
            if (type.value == mealType) {
                return true;
            }
        }
        return false;
    }
}