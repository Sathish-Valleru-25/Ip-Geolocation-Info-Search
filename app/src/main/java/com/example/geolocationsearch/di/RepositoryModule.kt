package com.example.geolocationsearch.di

import com.example.geolocationsearch.data.repos.IpAddressSearchRepositoryImpl
import com.example.geolocationsearch.data.repos.IpSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies related to the repository.
 */

@Module
@InstallIn(SingletonComponent :: class)
abstract  class RepositoryModule {

    @Binds
    @Singleton
    abstract  fun bindIpSearchRepository(ipSearchRepositoryImpl: IpAddressSearchRepositoryImpl): IpSearchRepository

}