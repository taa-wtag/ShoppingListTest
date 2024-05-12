package com.example.shoppinglisttest.repositories

import androidx.lifecycle.LiveData
import com.example.shoppinglisttest.data.local.ShoppingItem
import com.example.shoppinglisttest.data.remote.responses.ImageResponse
import com.example.shoppinglisttest.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}