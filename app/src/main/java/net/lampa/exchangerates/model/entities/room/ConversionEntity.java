package net.lampa.exchangerates.model.entities.room;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "conversion")
public class ConversionEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long dateInMillis;
    public float rate;
    public float result;
    public String baseCurrency;
    public String secondCurrency;

    public ConversionEntity(long dateInMillis, float rate, float result, String baseCurrency, String secondCurrency) {
        this.dateInMillis = dateInMillis;
        this.rate = rate;
        this.result = result;
        this.baseCurrency = baseCurrency;
        this.secondCurrency = secondCurrency;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public void setDateInMillis(long dateInMillis) {
        this.dateInMillis = dateInMillis;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrency = secondCurrency;
    }
}
