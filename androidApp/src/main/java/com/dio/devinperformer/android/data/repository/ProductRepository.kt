package com.dio.devinperformer.android.data.repository

import com.dio.devinperformer.android.data.model.Product
import com.dio.devinperformer.android.data.remote.RetrofitClient

class ProductRepository {

    private val api = RetrofitClient.api

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val products = api.getAllProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val product = api.getProductById(id)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<List<String>> {
        return try {
            val categories = api.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val products = api.getProductsByCategory(category)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
