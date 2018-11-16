package net.lampa.exchangerates.application;

import android.app.Application;

import net.lampa.exchangerates.di.components.ApplicationComponent;
import net.lampa.exchangerates.di.components.DaggerApplicationComponent;
import net.lampa.exchangerates.di.modules.ApplicationModule;

public class ExchangeRatesApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
