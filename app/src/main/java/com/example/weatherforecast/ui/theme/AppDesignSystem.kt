package com.example.fitway.ui.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

//colors
data class AppColorScheme(
    val background: Color,
    val onBackground: Color,
    val onBackground2: Color,
    val onBackground3: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    )

//typography
data class AppTypography(
    val titleLarge: TextStyle,
    val titleNormal: TextStyle,
    val body: TextStyle,
    val labelLarge: TextStyle,
    val labelNormal: TextStyle,
    val labelSmall: TextStyle,
)

//shapes
data class AppShape(
    val container: Shape,
    val button: Shape,
)

//size
data class AppSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp,
)

val LocalAppColorScheme = staticCompositionLocalOf<AppColorScheme> {
    AppColorScheme(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        onBackground2 = Color.Unspecified,
        onBackground3 = Color.Unspecified,
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    AppTypography(
        titleLarge = TextStyle(),
        titleNormal = TextStyle(),
        body = TextStyle(),
        labelLarge = TextStyle(),
        labelNormal = TextStyle(),
        labelSmall = TextStyle(),
    )
}

val LocalAppShape = staticCompositionLocalOf<AppShape> {
    AppShape(
        container = RectangleShape,
        button = RectangleShape,
    )
}

val LocalAppSize = staticCompositionLocalOf<AppSize> {
    AppSize(
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small = Dp.Unspecified,
    )
}