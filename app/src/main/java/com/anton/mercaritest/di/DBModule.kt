package com.anton.mercaritest.di

import android.content.Context
import androidx.room.Room
import com.anton.mercaritest.data.db.AppDatabase
import org.koin.dsl.module.module

val dbModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "allmember_room_db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        get<AppDatabase>().productsDao()
    }
}