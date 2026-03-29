package com.dio.devinperformer.android.ui.navigation

sealed class Screen(val route: String) {
    data object ProductList : Screen("product_list")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    data object Cart : Screen("cart")
}
