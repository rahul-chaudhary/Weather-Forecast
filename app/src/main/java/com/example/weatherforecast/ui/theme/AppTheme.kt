package com.example.fitway.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorScheme = AppColorScheme(
    background = Color(0xFF232531),
    onBackground = Color(0xFFf6f6f6),
    onBackground2 = Color(0xFF343747),
    onBackground3 = Color(0xFF343747),
    primary = Color(0xFF105f49),
    onPrimary = Color.White,
    secondary = Color(0xFFff9a62),
    onSecondary = Color(0xFF232531),
)

private val lightColorScheme = AppColorScheme(
    background = Color(0xFFf6f6f6),
    onBackground = Color(0xFF232531),
    onBackground2 = Color(0xFFd5d9e0),
    onBackground3 = Color(0xFFf2f4f7),
    primary = Color(0xFF105f49),
    onPrimary = Color.White,
    secondary = Color(0xFFe94e49),
    onSecondary = Color.White,

    )

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),
    titleNormal = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    body = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    labelNormal = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
)

private val shapes = AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(50),
)

private val sizes = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp,
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shapes,
        LocalAppSize provides sizes,
        LocalIndication provides rippleIndication,
        content = content
    )

}

object AppTheme {
    val colorsScheme: AppColorScheme
        @Composable
        get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val shapes: AppShape
        @Composable
        get() = LocalAppShape.current

    val sizes: AppSize
        @Composable
        get() = LocalAppSize.current
}