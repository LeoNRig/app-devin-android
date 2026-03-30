package com.dio.devinperformer.android.ui.screens.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dio.devinperformer.android.data.model.Product
import com.dio.devinperformer.android.ui.components.GradientSearchField
import com.dio.devinperformer.android.ui.theme.AppColors
import com.dio.devinperformer.android.viewmodel.CartViewModel
import com.dio.devinperformer.android.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    viewModel: ProductListViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartState by cartViewModel.uiState.collectAsState()
    val gradientBrush = Brush.horizontalGradient(
        listOf(AppColors.GradientStart, AppColors.GradientEnd)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DevinStore",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            // Animated Cart Badge
            val badgeScale by animateFloatAsState(
                targetValue = if (cartState.itemCount > 0) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "badgeScale"
            )

            BadgedBox(
                badge = {
                    if (cartState.itemCount > 0) {
                        Badge(
                            containerColor = AppColors.GradientStart,
                            contentColor = Color.White,
                            modifier = Modifier.scale(badgeScale)
                        ) {
                            Text(cartState.itemCount.toString())
                        }
                    }
                }
            ) {
                IconButton(onClick = onCartClick) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrinho",
                        tint = AppColors.TextPrimary
                    )
                }
            }
        }

        // Search Field
        GradientSearchField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = "Buscar produtos...",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryChip(
                    label = "Todos",
                    isSelected = uiState.selectedCategory == null,
                    onClick = { viewModel.selectCategory(null) }
                )
            }
            items(uiState.categories) { category ->
                CategoryChip(
                    label = category.replaceFirstChar { it.uppercase() },
                    isSelected = uiState.selectedCategory == category,
                    onClick = { viewModel.selectCategory(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.GradientStart)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "Erro desconhecido",
                            color = AppColors.Error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toque para tentar novamente",
                            color = AppColors.GradientStart,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { viewModel.loadProducts() }
                        )
                    }
                }
            }
            else -> {
                val filteredProducts = viewModel.getFilteredProducts()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(filteredProducts) { index, product ->
                        // Staggered fade-in + slide animation
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(product.id) {
                            kotlinx.coroutines.delay(index * 50L)
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                animationSpec = tween(durationMillis = 400)
                            ) + slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(durationMillis = 400)
                            )
                        ) {
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    // Animated background color transition
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.GradientStart else AppColors.CardBackground,
        animationSpec = tween(durationMillis = 300),
        label = "chipBgColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else AppColors.TextSecondary,
        animationSpec = tween(durationMillis = 300),
        label = "chipTextColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.GradientEnd else AppColors.GradientStart.copy(alpha = 0.5f),
        animationSpec = tween(durationMillis = 300),
        label = "chipBorderColor"
    )

    // Scale animation on selection
    val chipScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chipScale"
    )

    Box(
        modifier = Modifier
            .scale(chipScale)
            .clip(shape)
            .background(backgroundColor, shape)
            .border(1.dp, borderColor, shape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val gradientColors = listOf(AppColors.GradientStart, AppColors.GradientEnd)

    Column(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(AppColors.CardBackground, shape)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(gradientColors),
                shape = shape
            )
            .clip(shape)
            .clickable { onClick() }
    ) {
        // Product Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Product Info
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = product.title,
                color = AppColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AppColors.StarYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating.rate}",
                    color = AppColors.TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "R$ %.2f".format(product.price),
                color = AppColors.GradientStart,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
