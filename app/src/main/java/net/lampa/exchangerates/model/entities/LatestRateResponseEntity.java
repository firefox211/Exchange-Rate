package net.lampa.exchangerates.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class LatestRateResponseEntity {

    @SerializedName("date")
    private String date;
    @SerializedName("rates")
    private TreeMap<String, Float> rates;
    @SerializedName("base")
    private String base;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TreeMap<String, Float> getRates() {
        return rates;
    }

    public void setRates(TreeMap<String, Float> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public ArrayList<SimpleRateEntity> ratesToArrayList() {
        ArrayList<SimpleRateEntity> ratesArrayList = new ArrayList<>();
        if (rates != null) {

            for (Map.Entry<String, Float> rate: rates.entrySet()) {
                ratesArrayList.add(new SimpleRateEntity(rate.getKey(), rate.getValue()));
            }
        }
        return ratesArrayList;

    }
}


