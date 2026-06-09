package com.example.my4thhw.data.remote

import com.example.my4thhw.model.Manga
import com.example.my4thhw.model.MangaDetails
import com.google.gson.annotations.SerializedName

data class JikanMangaListResponse(
    val data: List<JikanMangaDto> = emptyList()
)

data class JikanMangaDetailsResponse(
    val data: JikanMangaDetailsDto
)

data class JikanMangaDto(
    @SerializedName("mal_id")
    val id: Int,
    val title: String? = null,

    @SerializedName("score")
    val rating: Double? = null,
    val genres: List<JikanNamedDto>? = null
)

data class JikanMangaDetailsDto(
    @SerializedName("mal_id")
    val id: Int,
    val title: String? = null,

    @SerializedName("score")
    val rating: Double? = null,
    val chapters: Int? = null,

    @SerializedName("synopsis")
    val description: String? = null,
    val genres: List<JikanNamedDto>? = null,
    val authors: List<JikanNamedDto>? = null
)

data class JikanNamedDto(
    val name: String? = null
)

fun JikanMangaDto.toDomainOrNull(): Manga? {
    val safeTitle = title ?: return null

    val genreString = genres
        ?.mapNotNull { it.name }
        ?.joinToString(", ")
        .orEmpty()
        .ifBlank { "Жанр неизвестен" }

    return Manga(
        id = id,
        title = safeTitle,
        genre = genreString,
        rating = rating
    )
}

fun JikanMangaDetailsDto.toDomain(): MangaDetails {
    val genreString = genres
        ?.mapNotNull { it.name }
        ?.joinToString(", ")
        .orEmpty()
        .ifBlank { "Жанр неизвестен" }

    val authorString = authors
        ?.mapNotNull { it.name }
        ?.joinToString(", ")
        .orEmpty()
        .ifBlank { "Автор неизвестен" }

    return MangaDetails(
        id = id,
        title = title ?: "Название неизвестно",
        genre = genreString,
        rating = rating,
        author = authorString,
        chapters = chapters,
        description = description ?: "Описание отсутствует"
    )
}