package com.dio.devinperformer.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dio.devinperformer.android.data.model.Product
import com.dio.devinperformer.android.data.repository.CartRepository
import com.dio.devinperformer.android.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val addedToCart: Boolean = false
)

class ProductDetailViewModel : ViewModel() {

    private val repository = ProductRepository()
    private val cartRepository = CartRepository.getInstance()

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getProductById(productId)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(
                        product = product,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erro ao carregar produto"
                    )
                }
        }
    }

    fun addToCart() {
        _uiState.value.product?.let { product ->
            cartRepository.addToCart(product)
            _uiState.value = _uiState.value.copy(addedToCart = true)
        }
    }

    fun resetCartStatus() {
        _uiState.value = _uiState.value.copy(addedToCart = false)
    }
}
