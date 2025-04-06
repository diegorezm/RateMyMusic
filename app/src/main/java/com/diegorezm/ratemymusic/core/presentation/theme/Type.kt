package com.diegorezm.ratemymusic.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.diegorezm.ratemymusic.R

private val TitilliumWeb = FontFamily(
    Font(R.font.titillium_web_regular, FontWeight.Normal),
    Font(R.font.titillium_web_bold, FontWeight.Bold),
    Font(R.font.titillium_web_black, FontWeight.Black),
    Font(R.font.titillium_web_semibold, FontWeight.SemiBold),
    Font(R.font.titillium_web_semibold, FontWeight.Medium),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)