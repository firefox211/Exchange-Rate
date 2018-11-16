package net.lampa.exchangerates.di.components;

import android.content.Context;

import net.lampa.exchangerates.di.modules.ApplicationModule;
import net.lampa.exchangerates.di.modules.DBModule;
import net.lampa.exchangerates.di.modules.NetworkModule;
import net.lampa.exchangerates.managers.SharedPreferencesManager;
import net.lampa.exchangerates.model.network.ApiRequestService;
import net.lampa.exchangerates.model.room.dao.ConversionDao;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, DBModule.class})
public interface ApplicationComponent {

    ApiRequestService apiRequestService();
    SharedPreferencesManager sharedPreferencesManager();
    ConversionDao conversionDao();
    Context context();
}
