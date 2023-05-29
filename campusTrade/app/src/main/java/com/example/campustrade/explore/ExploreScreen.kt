package com.example.campustrade.explore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.ui.theme.CampustradeTheme
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campustrade.R
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.ui.theme.white


class ExploreScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                ExploreScreenComposable()
            }
        }
    }
}


@Composable
fun ExploreScreenComposable(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.explore_top_bar_text),
                    style = MaterialTheme.typography.h2
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, HomeActivityMVVM::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            backgroundColor = white,
            contentColor = Color.Black,
        )
    }


}

@Preview
@Composable
fun LoginScreenPreview() {
    CampustradeTheme {
        ExploreScreenComposable()
    }
}













