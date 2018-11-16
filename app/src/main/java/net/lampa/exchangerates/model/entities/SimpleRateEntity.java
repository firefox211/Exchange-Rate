package net.lampa.exchangerates.model.entities;

public class SimpleRateEntity {

    private String name;
    private Long dateInMillis;
    private float rate;

    public SimpleRateEntity(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public SimpleRateEntity(String name, Long dateInMillis, float rate) {
        this.name = name;
        this.dateInMillis = dateInMillis;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public Long getDateInMillis() {
        return dateInMillis;
    }

    public float getRate() {
        return rate;
    }
}
