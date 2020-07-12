package com.example.eattreat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class], version = 1)
abstract class RestaurantDatabase1 : RoomDatabase(){

    abstract fun restaurantDao() : RestaurantDao



}