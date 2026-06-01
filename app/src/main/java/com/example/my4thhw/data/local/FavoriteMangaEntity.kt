package com.example.my4thhw.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my4thhw.model.Manga

@Entity("favourite_manga")
data class FavoriteMangaEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Double?
)

fun FavoriteMangaEntity.toDomain(): Manga = Manga(
    id,
    title,
    genre,
    rating,
    isFavourite = true,
)

fun Manga.toFavoriteEntity(): FavoriteMangaEntity = FavoriteMangaEntity(
    id,
    title,
    genre,
    rating,
)