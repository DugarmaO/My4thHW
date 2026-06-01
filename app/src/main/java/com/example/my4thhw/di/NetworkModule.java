package com.example.my4thhw.di;
import com.example.my4thhw.data.remote.JikanApi;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.inject.Singleton;

import com.example.my4thhw.data.remote.JikanApi;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "https://api.jikan.moe/v4/";

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public JikanApi provideJikanApi(Retrofit retrofit) {
        return retrofit.create(JikanApi.class);
    }
}