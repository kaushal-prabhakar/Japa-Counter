package com.kaushal.japacounter.di.modules

import android.content.Context
import com.kaushal.japacounter.data.db.RoomDB
import com.kaushal.japacounter.utils.UTCDateTimeAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRoomDataBase(@ApplicationContext context: Context) = RoomDB.getDataBase(context)

    @Provides
    @Singleton
    fun providesMoshi() : Moshi = Moshi.Builder()
        .add(UTCDateTimeAdapter())
        .build()
}