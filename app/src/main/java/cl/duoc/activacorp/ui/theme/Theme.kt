package cl.duoc.activacorp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Claro = lightColorScheme(primary = Color(0xFF00695C), secondary = Color(0xFFEF6C00), tertiary = Color(0xFF5E35B1))
private val Oscuro = darkColorScheme(primary = Color(0xFF4DB6AC), secondary = Color(0xFFFFB74D), tertiary = Color(0xFFB39DDB))

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Oscuro else Claro, content = content)
}
