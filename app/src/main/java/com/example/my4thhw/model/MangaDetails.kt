package com.example.my4thhw.model

data class MangaDetails(
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Double?,
    val author: String,
    val chapters: Int?,
    val description: String,
)
