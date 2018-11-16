package net.lampa.exchangerates.di.components;

import net.lampa.exchangerates.view.activities.ConversionActivity;
import net.lampa.exchangerates.view.activities.MainActivity;
import net.lampa.exchangerates.di.ScreenScope;
import net.lampa.exchangerates.view.activities.RateFinalActivity;

import dagger.Component;

@ScreenScope
@Component(dependencies = {ApplicationComponent.class})
public interface ScreenComponent {

    void inject(MainActivity mainActivity);

    void inject(RateFinalActivity rateFinalActivity);

    void inject(ConversionActivity conversionActivity);
}
