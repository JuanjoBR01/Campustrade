package com.example.campustrade.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.campustrade.R


val InriaSans = FontFamily(
    Font(R.font.inria_sans_regular),
    Font(R.font.inria_sans_bold, FontWeight.Bold)
)


val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = InriaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    h2 = TextStyle(
        fontFamily = InriaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = InriaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = InriaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)
/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
