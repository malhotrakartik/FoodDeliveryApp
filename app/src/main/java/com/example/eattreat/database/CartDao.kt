package com.example.eattreat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CartDao {
    @Insert
    fun insertCart(cartEntity: CartEntity)

    @Delete
    fun deleteCart(cartEntity: CartEntity)

    @Query("SELECT * FROM Cart")
    fun getAllCart() : List<CartEntity>

    @Query("DELETE FROM Cart WHERE DishId=:dish_id")
    fun deleteCartById(dish_id : String)


    @Query("SELECT * FROM Cart WHERE DishId=:dish_id")
    fun getCartById(dish_id : String): CartEntity






}