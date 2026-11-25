package com.vanlang.smartshop.repository

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/** Local demo storage. No server, payment gateway or customer-account sync. */
object ShopStorage {
    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.applicationContext.getSharedPreferences("smartshop_local", Context.MODE_PRIVATE)
    }

    fun readCart(): Map<Int, Int> = try {
        val json = JSONObject(prefs.getString("cart", "{}") ?: "{}")
        buildMap {
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val id = key.toIntOrNull() ?: continue
                val quantity = json.optInt(key, 0)
                if (id > 0 && quantity > 0) put(id, quantity)
            }
        }
    } catch (_: Exception) { emptyMap() }

    fun saveCart(items: Map<Int, Int>) {
        val json = JSONObject()
        items.forEach { (id, quantity) -> if (id > 0 && quantity > 0) json.put(id.toString(), quantity) }
        prefs.edit().putString("cart", json.toString()).apply()
    }

    fun readFavorites(): Set<Int> = try {
        val json = JSONArray(prefs.getString("favorites", "[]") ?: "[]")
        buildSet {
            for (i in 0 until json.length()) {
                val id = json.optInt(i, 0)
                if (id > 0) add(id)
            }
        }
    } catch (_: Exception) { emptySet() }

    fun saveFavorites(ids: Set<Int>) {
        val json = JSONArray()
        ids.sorted().forEach { if (it > 0) json.put(it) }
        prefs.edit().putString("favorites", json.toString()).apply()
    }

    fun readOrderJson(): String = prefs.getString("orders", "[]") ?: "[]"

    /** Commit order before removing the cart. If storage fails, don't lose the cart. */
    fun saveOrderJson(json: String): Boolean =
        prefs.edit().putString("orders", json).commit()
}
