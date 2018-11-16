package net.lampa.exchangerates.di.modules;


import android.content.Context;

import net.lampa.exchangerates.application.ExchangeRatesApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {


    private ExchangeRatesApplication context;

    public ApplicationModule(ExchangeRatesApplication context){
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return context;
    }

}
