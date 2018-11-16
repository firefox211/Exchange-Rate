package net.lampa.exchangerates.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getDateFromServerFormat;

public class HistoryResponseEntity {

    @SerializedName("start_at")
    private String startAt;
    @SerializedName("end_at")
    private String endAt;
    @SerializedName("rates")
    private Map<String, Map<String, Float>> rates;
    @SerializedName("base")
    private String base;

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public Map<String, Map<String, Float>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Float>> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public ArrayList<SimpleRateEntity> getItemHistoryArray(String rateName) {
        ArrayList<SimpleRateEntity> itemHistoryArray = new ArrayList<>();
        if (rates != null) {
            for (Map.Entry<String, Map<String, Float>> rate : rates.entrySet()) {
                if (rate != null && rate.getValue() != null && rate.getValue().containsKey(rateName)) {
                    itemHistoryArray.add(
                            new SimpleRateEntity(rateName,
                                    getDateFromServerFormat(rate.getKey()).getTime(),
                                    rate.getValue().get(rateName)));
                }
            }
        }

        Collections.sort(itemHistoryArray, (o1, o2) -> o1.getDateInMillis().compareTo(o2.getDateInMillis()));

        return itemHistoryArray;

    }
}
