package com.vanlang.smartshop.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// =====================================
// API RESPONSE
// =====================================

data class ProductApiResponse(
    val products: List<ProductApiDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

// =====================================
// PRODUCT DTO
// =====================================

data class ProductApiDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String,
    val images: List<String> = emptyList()
)

// =====================================
// PRODUCT API INTERFACE
// =====================================

interface ProductApi {

    // Lấy danh sách sản phẩm
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): ProductApiResponse

    // Lấy chi tiết sản phẩm theo ID
    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: Int
    ): ProductApiDto

    // Tìm kiếm sản phẩm
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") keyword: String
    ): ProductApiResponse
}

// =====================================
// RETROFIT CLIENT
// =====================================

object ProductApiClient {

    private const val BASE_URL =
        "https://dummyjson.com/"

    val api: ProductApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ProductApi::class.java)
    }
}
