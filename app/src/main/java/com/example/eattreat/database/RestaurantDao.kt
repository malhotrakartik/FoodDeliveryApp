package com.example.eattreat.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM Restaurants")
    fun getAllRestaurants() : List<RestaurantEntity>

    @Query("DELETE FROM Restaurants WHERE restaurantId=:restaurantId")
    fun deleteRestaurantById(restaurantId : String)


    @Query("SELECT * FROM Restaurants WHERE restaurantId=:restaurant_id")
    fun getRestaurantById(restaurant_id : String): RestaurantEntity

}