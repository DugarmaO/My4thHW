package com.example.my4thhw.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApi {

    @GET("manga")
    suspend fun getMangaList(
        @Query("page") page: Int = 1
    ): JikanMangaListResponse

    @GET("manga")
    suspend fun searchManga(
        @Query("q") query: String
    ): JikanMangaListResponse

    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") id: Int
    ): JikanMangaDetailsResponse
}