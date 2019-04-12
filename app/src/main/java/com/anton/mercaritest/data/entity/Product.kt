package com.anton.mercaritest.data.entity

import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val name: String? = null,
    val status: String? = null,
    @SerializedName("num_likes")
    val numLikes: Long = 0,
    @SerializedName("num_comments")
    val numComments: Long = 0,
    val price: Long = 0,
    val photo: String? = null
)