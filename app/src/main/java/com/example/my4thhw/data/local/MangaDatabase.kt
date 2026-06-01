package com.example.my4thhw.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteMangaEntity::class],
    version = 1,
)

abstract class MangaDatabase : RoomDatabase() {
    abstract fun mangaDao(): MangaDao
}