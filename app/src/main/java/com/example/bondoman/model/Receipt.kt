package com.example.bondoman.model
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("name") val name: String,
    @SerializedName("qty") val qty: Int,
    @SerializedName("price") val price: Double
)

data class ItemsResponse(
    @SerializedName("items") val items: List<Item>
)

data class Receipt(
    @SerializedName("items") val items: ItemsResponse
)
