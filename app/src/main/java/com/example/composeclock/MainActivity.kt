package com.example.composeclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeclock.ui.theme.ComposeClockTheme
import kotlinx.coroutines.delay
import java.util.Calendar

//https://medium.com/google-developer-experts/compose-oclock-50c778a6360
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeClockTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    ClockScreen()
}

@Composable
fun ClockScreen() {
    fun currentTime(): MyTime {
        val cal = Calendar.getInstance()
        return MyTime(
            hours = cal.get(Calendar.HOUR_OF_DAY),
            minutes = cal.get(Calendar.MINUTE),
            seconds = cal.get(Calendar.SECOND),
        )
    }

    var time by remember { mutableStateOf(currentTime()) }
    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    MyClock(time)
}

@Composable
fun MyClock(time: MyTime) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val pad = Modifier.padding(horizontal = 5.dp)
        MyColumnOfNumbers(0..2, time.hours / 10, pad)
        MyColumnOfNumbers(0..9, time.hours % 10, pad)
        Spacer(modifier = Modifier.size(20.dp))
        MyColumnOfNumbers(0..5, time.minutes / 10, pad)
        MyColumnOfNumbers(0..9, time.minutes % 10, pad)
        Spacer(modifier = Modifier.size(20.dp))
        MyColumnOfNumbers(0..5, time.seconds / 10, pad)
        MyColumnOfNumbers(0..9, time.seconds % 10, pad)


    }
}

@Composable
fun MyColumnOfNumbers(

    range: IntRange,
    current: Int,
    modifier: Modifier = Modifier
) {
    val size = 50.dp
    val reset = current == range.first
    val medium = (range.last - range.first) / 2f
    val offset by animateDpAsState(
        targetValue = size * (medium - current),
        animationSpec = if (reset) {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow,
            )
        } else {
            tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
            )
        }
    )
    Column(
        modifier = modifier
            .offset(y = offset)
            .clip(RoundedCornerShape(25.dp))

    ) {
        range.forEach {
            Number(it, it == current, Modifier.size(size))
        }


    }
}

@Composable
fun Number(value: Int, active: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor by animateColorAsState(
        if (active) {
            Color.Cyan
        } else {
            Color.Green
        }
    )


    Box(
        modifier = modifier
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            modifier = Modifier

                .align(Alignment.Center),
            fontSize = 20.sp,
            color = Color.White,

            )
    }
}

data class MyTime(val hours: Int, val minutes: Int, val seconds: Int)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeClockTheme {
        Greeting("Android")
    }
}