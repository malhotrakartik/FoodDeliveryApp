package com.example.eattreat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")
data class CartEntity(
    @PrimaryKey val DishId: String,

    @ColumnInfo(name="dish_name")val DishName : String,
    @ColumnInfo(name = "dish_price")val DishPrice : String
)