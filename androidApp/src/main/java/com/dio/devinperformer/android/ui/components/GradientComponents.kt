package com.dio.devinperformer.android.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dio.devinperformer.android.ui.theme.AppColors

@Composable
fun GradientSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Buscar produtos...",
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    // Animated gradient shimmer
    val infiniteTransition = rememberInfiniteTransition(label = "searchShimmer")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val animatedBrush = Brush.horizontalGradient(
        colors = listOf(
            AppColors.GradientStart,
            AppColors.GradientEnd,
            AppColors.GradientStart,
            AppColors.GradientEnd
        ),
        startX = offset,
        endX = offset + 500f,
        tileMode = TileMode.Mirror
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.DarkGray
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(Color.White, shape)
            .border(
                width = 2.dp,
                brush = animatedBrush,
                shape = shape
            )
            .padding(horizontal = 20.dp, vertical = 18.dp),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animated gradient shimmer for button background
    val infiniteTransition = rememberInfiniteTransition(label = "buttonShimmer")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "buttonShimmerOffset"
    )

    val animatedGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            AppColors.GradientStart,
            AppColors.GradientEnd,
            AppColors.GradientStart,
            AppColors.GradientEnd
        ),
        startX = offset,
        endX = offset + 500f,
        tileMode = TileMode.Mirror
    )

    // Scale + Bounce on press
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = 400f
        ),
        label = "buttonScale"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .background(animatedGradientBrush, RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
