package com.example.my4thhw.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my4thhw.model.Manga
import com.example.my4thhw.model.MangaDetails

@Entity("favourite_manga")
data class FavoriteMangaEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Double?,
    val author: String,
    val chapters: Int?,
    val description: String,
)

fun FavoriteMangaEntity.toDomain(): Manga = Manga(
    id,
    title,
    genre,
    rating,
    isFavourite = true,
)

fun FavoriteMangaEntity.toDetailsDomain(): MangaDetails = MangaDetails(
    id,
    title,
    genre,
    rating,
    author,
    chapters,
    description,
)

fun MangaDetails.toFavoriteEntity(): FavoriteMangaEntity = FavoriteMangaEntity(
    id,
    title,
    genre,
    rating,
    author,
    chapters,
    description,
)

fun Manga.toFavoriteEntity(): FavoriteMangaEntity = FavoriteMangaEntity(
    id,
    title,
    genre,
    rating,
    author = "",
    chapters = null,
    description = "",
)