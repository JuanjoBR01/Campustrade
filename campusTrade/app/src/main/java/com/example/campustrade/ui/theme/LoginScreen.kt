package com.example.campustrade.ui.theme

import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.R
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Column {
        Text(
            text = stringResource(id = R.string.app_title),
            style = MaterialTheme.typography.h1,
            modifier = modifier.padding(top = 8.dp)
        )
    }

}


@Preview
@Composable
fun LoginScreenPreview() {
    CampustradeTheme {
        LoginScreen()
    }
}

