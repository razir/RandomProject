package com.anton.mercaritest.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val PRODUCT_STATUS_SOLD_OUT = "sold_out"

@Entity
class Product {
    @PrimaryKey
    var id: String = ""
    var name: String? = null
    var status: String? = null
    @SerializedName("num_likes")
    var numLikes: Long = 0
    @SerializedName("num_comments")
    var numComments: Long = 0
    var price: Long = 0
    var photo: String? = null

    var categoryHash: String? = null
}