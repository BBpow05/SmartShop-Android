
package com.vanlang.smartshop.repository

import android.content.Context
import com.vanlang.smartshop.data.local.AppDatabase
import com.vanlang.smartshop.data.local.dao.ProductDao
import com.vanlang.smartshop.data.local.entity.ProductEntity
import com.vanlang.smartshop.data.remote.ProductApiClient
import com.vanlang.smartshop.data.remote.ProductApiDto
import com.vanlang.smartshop.model.Product
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToLong

object ProductRepository {

    // =====================================
    // 1. ROOM DATABASE
    // =====================================

    @Volatile
    private var productDao: ProductDao? = null

    fun initialize(context: Context) {

        if (productDao == null) {
            synchronized(this) {
                if (productDao == null) {
                    productDao = AppDatabase
                        .getInstance(context.applicationContext)
                        .productDao()
                }
            }
        }
    }

    private fun dao(): ProductDao {
        return checkNotNull(productDao) {
            "ProductRepository chưa được khởi tạo. " +
                    "Hãy gọi ProductRepository.initialize(context) trước."
        }
    }

    // =====================================
    // 2. API CLIENT
    // =====================================

    private val api by lazy {
        ProductApiClient.api
    }

    // Sản phẩm API được đặt ID riêng để không ghi đè
    // 20 sản phẩm mẫu có ID từ 1 đến 20.
    private const val API_ID_OFFSET = 100000

    // Tỷ giá giả lập phục vụ demo API.
    // Thay bằng giá VND từ backend thực tế khi triển khai.
    private const val DEMO_USD_TO_VND = 25000.0

    // =====================================
    // 3. LOCAL SAMPLE PRODUCTS
    // =====================================

    private val sampleProducts = listOf(

        Product(
            1, "Điện thoại Samsung",
            5990000L, "Điện thoại", "📱"
        ),

        Product(
            2, "Tai nghe Bluetooth",
            450000L, "Phụ kiện", "🎧"
        ),

        Product(
            3, "Laptop văn phòng",
            12990000L, "Laptop", "💻"
        ),

        Product(
            4, "Đồng hồ thông minh",
            990000L, "Phụ kiện", "⌚"
        ),

        Product(
            5, "Bàn phím cơ",
            750000L, "Phụ kiện", "⌨️"
        ),

        Product(
            6, "Máy tính bảng",
            4990000L, "Máy tính bảng", "📲"
        ),

        Product(
            7, "iPhone 15",
            15990000L, "Điện thoại", "📱"
        ),

        Product(
            8, "Ốp lưng điện thoại",
            120000L, "Phụ kiện", "📱"
        ),

        Product(
            9, "Sạc nhanh USB-C",
            250000L, "Phụ kiện", "🔌"
        ),

        Product(
            10, "Chuột không dây",
            320000L, "Phụ kiện", "🖱️"
        ),

        Product(
            11, "Màn hình máy tính",
            3290000L, "Máy tính", "🖥️"
        ),

        Product(
            12, "Loa Bluetooth",
            690000L, "Âm thanh", "🔊"
        ),

        Product(
            13, "Webcam Full HD",
            590000L, "Phụ kiện", "📷"
        ),

        Product(
            14, "Ổ cứng SSD 1TB",
            1890000L, "Linh kiện", "💾"
        ),

        Product(
            15, "USB 64GB",
            180000L, "Lưu trữ", "💽"
        ),

        Product(
            16, "Micro thu âm",
            850000L, "Âm thanh", "🎙️"
        ),

        Product(
            17, "Máy in văn phòng",
            2490000L, "Thiết bị văn phòng", "🖨️"
        ),

        Product(
            18, "Tay cầm chơi game",
            790000L, "Gaming", "🎮"
        ),

        Product(
            19, "Đế tản nhiệt laptop",
            350000L, "Phụ kiện", "💻"
        ),

        Product(
            20, "Pin sạc dự phòng",
            490000L, "Phụ kiện", "🔋"
        )
    )

    // Cache để tương thích với các Manager cũ.
    // Dữ liệu chính thức vẫn được lấy từ Room.
    @Volatile
    private var cachedProducts: List<Product> = sampleProducts

    val products: List<Product>
        get() = cachedProducts

    // =====================================
    // 4. SEED LOCAL DATA
    // =====================================

    suspend fun seedLocalProductsIfEmpty() {

        val currentCount = dao()
            .countProducts()
            .first()

        if (currentCount == 0) {

            val entities = sampleProducts.map { product ->
                ProductEntity.fromProduct(product)
            }

            dao().insertAllProducts(entities)

        }
    }

    // =====================================
    // 5. OBSERVE ALL PRODUCTS FROM ROOM
    // =====================================

    fun observeAllProducts(): Flow<List<Product>> {

        return dao()
            .getAllProducts()
            .map { entities ->
                entities.map { it.toProduct() }
            }
            .onEach { products ->
                cachedProducts = products
            }
    }

    // =====================================
    // 6. SEARCH & FILTER FROM ROOM
    // =====================================

    fun observeSearchProducts(
        keyword: String = "",
        category: String = "Tất cả"
    ): Flow<List<Product>> {

        val selectedCategory = category.takeIf {
            it != "Tất cả"
        }

        return dao()
            .searchProducts(
                keyword = keyword.trim(),
                category = selectedCategory
            )
            .map { entities ->
                entities.map { it.toProduct() }
            }
            .onEach { products ->
                cachedProducts = products
            }
    }

    // =====================================
    // 7. OBSERVE PRODUCT DETAIL
    // =====================================

    fun observeProductById(
        id: Int
    ): Flow<Product?> {

        return dao()
            .getProductById(id)
            .map { entity ->
                entity?.toProduct()
            }
    }

    // =====================================
    // 8. GET PRODUCT BY ID
    // Tương thích với CartRepository cũ
    // =====================================

    fun getProductById(id: Int): Product? {

        return cachedProducts.find { it.id == id }
            ?: sampleProducts.find { it.id == id }
    }

    // =====================================
    // 9. SEARCH PRODUCTS
    // Tương thích với Activity cũ
    // =====================================

    fun searchProducts(
        keyword: String = "",
        category: String = "Tất cả"
    ): List<Product> {

        return cachedProducts.filter { product ->

            val matchName = product.name.contains(
                keyword.trim(),
                ignoreCase = true
            )

            val matchCategory =
                category == "Tất cả" ||
                        product.category.equals(
                            category,
                            ignoreCase = true
                        )

            matchName && matchCategory
        }
    }

    // =====================================
    // 10. GET CATEGORIES
    // =====================================

    fun getCategories(): List<String> {

        return listOf("Tất cả") +
                cachedProducts
                    .map { it.category }
                    .distinct()
    }

    // =====================================
    // 11. OBSERVE CATEGORIES FROM ROOM
    // =====================================

    fun observeCategories(): Flow<List<String>> {

        return observeAllProducts().map { products ->

            listOf("Tất cả") +
                    products
                        .map { it.category }
                        .distinct()
        }
    }

    // =====================================
    // 12. REFRESH PRODUCTS FROM API
    // =====================================

    suspend fun refreshProductsFromApi(): Result<Int> {

        return try {

            val response = api.getProducts(
                limit = 30,
                skip = 0
            )

            val entities = response.products.map { dto ->

                ProductEntity.fromProduct(
                    dto.toLocalProduct()
                )
            }

            // Chỉ cập nhật sản phẩm lấy từ API.
            // ID bắt đầu từ 100001 nên không ghi đè
            // 20 sản phẩm mẫu ban đầu.

            dao().insertAllProducts(entities)

            Result.success(entities.size)

        } catch (exception: CancellationException) {

            throw exception

        } catch (exception: Exception) {

            Result.failure(exception)
        }
    }

    // =====================================
    // 13. INITIALIZE DATA
    // =====================================

    suspend fun initializeData(): Result<Int> {

        // Luôn tạo dữ liệu mẫu nếu Room chưa có dữ liệu.
        seedLocalProductsIfEmpty()

        // Sau đó thử tải dữ liệu trực tuyến.
        // Nếu mạng lỗi, dữ liệu Room vẫn được giữ nguyên.
        return refreshProductsFromApi()
    }

    // =====================================
    // 14. INSERT PRODUCT
    // =====================================

    suspend fun insertProduct(product: Product) {

        dao().insertProduct(
            ProductEntity.fromProduct(product)
        )
    }

    // =====================================
    // 15. UPDATE PRODUCT
    // =====================================

    suspend fun updateProduct(product: Product) {

        dao().updateProduct(
            ProductEntity.fromProduct(product)
        )
    }

    // =====================================
    // 16. DELETE PRODUCT
    // =====================================

    suspend fun deleteProduct(product: Product) {

        dao().deleteProduct(
            ProductEntity.fromProduct(product)
        )
    }

    // =====================================
    // 17. API DTO TO PRODUCT MODEL
    // =====================================

    private fun ProductApiDto.toLocalProduct(): Product {

        val mappedCategory = when {
            category.contains("smartphone", true) ||
                    category.contains("mobile", true) ->
                "Điện thoại"

            category.contains("laptop", true) ->
                "Laptop"

            category.contains("tablet", true) ->
                "Máy tính bảng"

            else ->
                category.replaceFirstChar { it.uppercase() }
        }

        val emoji = when (mappedCategory) {
            "Điện thoại" -> "📱"
            "Laptop" -> "💻"
            "Máy tính bảng" -> "📲"
            "Phụ kiện" -> "🎧"
            else -> "🛍️"
        }

        val image = thumbnail.ifBlank {
            images.firstOrNull().orEmpty()
        }

        return Product(
            id = API_ID_OFFSET + id,
            name = title,
            price = (price * DEMO_USD_TO_VND).roundToLong(),
            category = mappedCategory,
            emoji = emoji,
            imageUrl = image,
            description = description
        )
    }
}
