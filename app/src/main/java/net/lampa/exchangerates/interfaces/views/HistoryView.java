package net.lampa.exchangerates.interfaces.views;

import net.lampa.exchangerates.model.entities.SimpleRateEntity;

import java.util.ArrayList;

public interface HistoryView extends BaseView {

    void showHistory(ArrayList<SimpleRateEntity> itemHistoryArray);
}
