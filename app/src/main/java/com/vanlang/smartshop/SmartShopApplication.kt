package com.vanlang.smartshop

import android.app.Application
import com.vanlang.smartshop.repository.CartRepository
import com.vanlang.smartshop.repository.FavoriteRepository
import com.vanlang.smartshop.repository.OrderRepository
import com.vanlang.smartshop.repository.ShopStorage

class SmartShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ShopStorage.initialize(this)
        CartRepository.restore()
        FavoriteRepository.restore()
        OrderRepository.restore()
    }
}
