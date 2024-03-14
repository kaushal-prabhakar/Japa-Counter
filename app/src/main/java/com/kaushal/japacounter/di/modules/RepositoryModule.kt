package com.kaushal.japacounter.di.modules

import com.kaushal.japacounter.data.repository.MainRepository
import com.kaushal.japacounter.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: MainRepositoryImpl): MainRepository
}