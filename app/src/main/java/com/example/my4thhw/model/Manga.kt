package com.example.my4thhw.model

data class Manga (
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Double?,
    val isFavourite: Boolean = false,
)