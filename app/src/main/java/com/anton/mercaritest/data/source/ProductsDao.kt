package com.anton.mercaritest.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anton.mercaritest.data.entity.Product

@Dao
interface ProductsDao {

    @Query("SELECT * FROM product WHERE categoryHash == :categoryHash")
    fun getAllByCategory(categoryHash: String): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: List<Product>)

    @Query("DELETE FROM product WHERE categoryHash == :categoryHash")
    fun deleteWithCategory(categoryHash: String)

    @Query("DELETE FROM product")
    fun deleteAll()
}