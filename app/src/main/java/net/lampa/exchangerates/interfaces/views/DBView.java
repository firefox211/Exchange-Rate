package net.lampa.exchangerates.interfaces.views;

import net.lampa.exchangerates.model.entities.room.ConversionEntity;

import java.util.List;

public interface DBView extends BaseView {

    void showConversionHistory(List<ConversionEntity> conversions);

    void onHistoryCleared();

    void onConversionAdded();

    void onConversionRemoved();
}
