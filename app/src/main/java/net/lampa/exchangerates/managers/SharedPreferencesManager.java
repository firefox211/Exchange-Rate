package net.lampa.exchangerates.managers;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {

    private static final String PREFERENCES_FILE_NAME = "net.lampa.exchangerates.sp";
    private static final String BASE_CURRENCY = "base_currency";

    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setBaseCurrency(String currency) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BASE_CURRENCY, currency);
        editor.apply();
    }

    public String getBaseCurrency() {
        return sharedPreferences.getString(BASE_CURRENCY, "EUR");
    }


}
