package com.example.my4thhw.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MangaDao {

    @Query("SELECT * FROM favourite_manga ORDER BY title")
    suspend fun getFavourites(): List<FavoriteMangaEntity>

    @Query("SELECT id FROM favourite_manga")
    suspend fun getFavouritesIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(manga: FavoriteMangaEntity)

    @Query("DELETE FROM favourite_manga WHERE id = :id")
    suspend fun deleteById(id: Int)

}