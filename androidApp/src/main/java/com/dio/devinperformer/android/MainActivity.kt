package com.dio.devinperformer.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GradientInputScreen()
        }
    }
}

@Composable
fun GradientInputScreen() {
    // Cores baseadas no seu prompt
    val backgroundColor = Color(0xFF181818)
    val gradientColors = listOf(Color(0xFF43CEA2), Color(0xFF185A9D))

    // Estado para armazenar o texto digitado
    var text by remember { mutableStateOf("") }

    // Fundo da tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        // Configuração do Input
        val shape = RoundedCornerShape(12.dp)

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.DarkGray
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.85f) // Ocupa 85% da largura da tela
                .shadow(
                    elevation = 8.dp,
                    shape = shape,
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .background(Color.White, shape)
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = shape
                )
                .padding(horizontal = 20.dp, vertical = 18.dp), // Espaçamento interno
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    // Placeholder condicional
                    if (text.isEmpty()) {
                        Text(
                            text = "Digite aqui...",
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }
                    // O campo de texto real
                    innerTextField()
                }
            }
        )
    }
}

// Preview para testar rapidamente no Android Studio
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun GradientInputScreenPreview() {
    GradientInputScreen()
}
