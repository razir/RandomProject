package com.anton.mercaritest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.data.source.ProductsDao

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}