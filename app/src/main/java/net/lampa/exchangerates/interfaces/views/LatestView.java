package net.lampa.exchangerates.interfaces.views;

import net.lampa.exchangerates.model.entities.SimpleRateEntity;

import java.util.ArrayList;

public interface LatestView extends BaseView {

    void showLatestRates(ArrayList<SimpleRateEntity> rates, String date, String base);

}
