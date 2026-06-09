package com.example.my4thhw.data

import com.example.my4thhw.data.local.MangaDao
import com.example.my4thhw.data.local.toDomain
import com.example.my4thhw.data.local.toFavoriteEntity
import com.example.my4thhw.data.remote.JikanApi
import com.example.my4thhw.data.remote.toDomain
import com.example.my4thhw.data.remote.toDomainOrNull
import com.example.my4thhw.model.Manga
import com.example.my4thhw.model.MangaDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.my4thhw.data.local.toDetailsDomain
class MangaRepository @Inject constructor(
    private val api: JikanApi,
    private val mangaDao: MangaDao,
) {
    suspend fun getFavourites(): List<Manga> = withContext(Dispatchers.IO) {
        mangaDao.getFavourites().map { it.toDomain() }
    }

    suspend fun toggleFavourite(mangaDetails: MangaDetails) = withContext(Dispatchers.IO) {
        val existing = mangaDao.getById(mangaDetails.id)
        if (existing != null) {
            mangaDao.deleteById(mangaDetails.id)
        } else {
            mangaDao.upsert(mangaDetails.toFavoriteEntity())
        }
    }
    suspend fun toggleFavourite(manga: Manga) = withContext(Dispatchers.IO) {
        val existing = mangaDao.getById(manga.id)
        if (existing != null) {
            mangaDao.deleteById(manga.id)
        } else {
            mangaDao.upsert(manga.toFavoriteEntity())
        }
    }
    suspend fun isFavourite(id: Int): Boolean = withContext(Dispatchers.IO) {
        mangaDao.getById(id) != null
    }

    suspend fun getMangaList(): List<Manga> = withContext(Dispatchers.IO) {
        val favouriteIds = mangaDao.getFavouritesIds().toSet()
        api.getMangaList().data
            .mapNotNull { it.toDomainOrNull() }
            .map { manga -> manga.copy(isFavourite = manga.id in favouriteIds) }
    }

    suspend fun searchManga(query: String): List<Manga> = withContext(Dispatchers.IO) {
        val favouriteIds = mangaDao.getFavouritesIds().toSet()
        api.searchManga(query).data
            .mapNotNull { it.toDomainOrNull() }
            .map { manga -> manga.copy(isFavourite = manga.id in favouriteIds) }
    }

    suspend fun getDetails(id: Int): MangaDetails = withContext(Dispatchers.IO) {
        api.getMangaDetails(id).data.toDomain()
    }

    suspend fun getDetailsFromCache(id: Int): MangaDetails? = withContext(Dispatchers.IO) {
        mangaDao.getById(id)?.toDetailsDomain()
    }
}