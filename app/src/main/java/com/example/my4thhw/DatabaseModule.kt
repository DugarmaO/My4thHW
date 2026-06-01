package com.example.my4thhw

import android.content.Context
import androidx.room.Room
import com.example.my4thhw.data.local.MangaDao
import com.example.my4thhw.data.local.MangaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providMangaDatabase(
        @ApplicationContext context: Context
    ): MangaDatabase =
        Room.databaseBuilder(
            context,
            MangaDatabase::class.java,
            "manga.db",
        ).build()

    @Provides
    @Singleton
    fun provideMangaDao(
        database: MangaDatabase
    ): MangaDao = database.mangaDao()

}