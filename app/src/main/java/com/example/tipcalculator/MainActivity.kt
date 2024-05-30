package com.example.tipcalculator

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Tiplayout()
                }
            }
        }
    }
}

@Composable
fun Tiplayout (modifier: Modifier = Modifier.fillMaxSize()) {
    var amountInput by remember { mutableStateOf("") }
    var percentState by remember{ mutableStateOf("") }
    var switchState by remember{ mutableStateOf(false) }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val percent = percentState.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, percent, switchState)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),

    ) {
        Text(
            text = "calculator tip",
            modifier = modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(value = amountInput,
            onValueChange = {amountInput = it},
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            leadingIcon = R.drawable.money,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())
        EditNumberField(value = percentState,
            onValueChange = {percentState = it},
            label = R.string.tip_percentage,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            leadingIcon = R.drawable.percent,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())
        RoundTip(roundUp = switchState,
            onCheckedChange = {switchState = it}, modifier = modifier.padding(bottom = 32.dp))
        Text(
            text = stringResource(R.string.tipamount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}
@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if(roundUp) {
      tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun EditNumberField(value: String,
                    onValueChange: (String) -> Unit,
                    @StringRes label: Int,
                    keyboardOptions: KeyboardOptions,
                    @DrawableRes leadingIcon: Int,
                    modifier: Modifier = Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(stringResource(id = label))},
        singleLine = true,
        keyboardOptions = keyboardOptions,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null)},
        modifier = modifier)
}

@Composable
fun RoundTip(roundUp: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Round The Tip?")
        Switch(
            checked = roundUp,
            onCheckedChange = onCheckedChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(top = 25.dp),

        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        Tiplayout()
    }
}