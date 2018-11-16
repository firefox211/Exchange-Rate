package net.lampa.exchangerates.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import net.lampa.exchangerates.model.room.AppDatabase;
import net.lampa.exchangerates.model.room.dao.ConversionDao;

import dagger.Module;
import dagger.Provides;

@Module
public class DBModule {

    @Provides
    AppDatabase provideAppDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "exchange-rates-app-db").build();
    }

    @Provides
    ConversionDao provideConversionDao(AppDatabase appDatabase) {
        return appDatabase.conversionDao();
    }

}
