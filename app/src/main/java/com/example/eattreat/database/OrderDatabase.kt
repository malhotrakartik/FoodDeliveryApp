package com.example.eattreat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class], version = 1)
abstract class OrderDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
}