package net.lampa.exchangerates.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.lampa.exchangerates.application.ExchangeRatesApplication;
import net.lampa.exchangerates.di.components.ApplicationComponent;
import net.lampa.exchangerates.utils.Layout;

import java.lang.annotation.Annotation;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getClass().isAnnotationPresent(Layout.class)) {
            Annotation annotation = getClass().getAnnotation(Layout.class);
            setContentView(((Layout) annotation).id());
            unbinder = ButterKnife.bind(this);
        }

        inject();
    }


    public ApplicationComponent getApplicationComponent() {
        return ((ExchangeRatesApplication) getApplication()).getApplicationComponent();
    }

    protected abstract void inject();

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
    }
}
