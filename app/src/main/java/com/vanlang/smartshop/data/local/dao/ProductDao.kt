
package com.vanlang.smartshop.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vanlang.smartshop.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // =====================================
    // GET ALL PRODUCTS
    // =====================================

    @Query(
        "SELECT * FROM products ORDER BY name COLLATE NOCASE ASC"
    )
    fun getAllProducts(): Flow<List<ProductEntity>>


    // =====================================
    // GET PRODUCT BY ID
    // =====================================

    @Query(
        "SELECT * FROM products WHERE id = :productId LIMIT 1"
    )
    fun getProductById(
        productId: Int
    ): Flow<ProductEntity?>


    // =====================================
    // SEARCH & FILTER PRODUCTS
    // =====================================

    @Query(
        """
        SELECT * FROM products
        WHERE (:category IS NULL OR category = :category)
        AND (
            :keyword = ''
            OR name LIKE '%' || :keyword || '%'
        )
        ORDER BY name COLLATE NOCASE ASC
        """
    )
    fun searchProducts(
        keyword: String,
        category: String?
    ): Flow<List<ProductEntity>>


    // =====================================
    // INSERT PRODUCT
    // =====================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(
        product: ProductEntity
    )


    // =====================================
    // INSERT MANY PRODUCTS
    // =====================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(
        products: List<ProductEntity>
    )


    // =====================================
    // UPDATE PRODUCT
    // =====================================

    @Update
    suspend fun updateProduct(
        product: ProductEntity
    )


    // =====================================
    // DELETE PRODUCT
    // =====================================

    @Delete
    suspend fun deleteProduct(
        product: ProductEntity
    )


    // =====================================
    // DELETE ALL PRODUCTS
    // =====================================

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()


    // =====================================
    // COUNT PRODUCTS
    // =====================================

    @Query("SELECT COUNT(*) FROM products")
    fun countProducts(): Flow<Int>
}
