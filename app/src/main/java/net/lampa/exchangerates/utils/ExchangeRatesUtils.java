package net.lampa.exchangerates.utils;

import net.lampa.exchangerates.model.entities.SimpleRateEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExchangeRatesUtils {

    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";
    public static final String USER_DATE_FORMAT = "dd.MM.yyyy";
    public static final String USER_FULL_DATE_FORMAT = "HH:mm:ss dd.MM.yyyy";

    public static ArrayList<String> getRateNames(ArrayList<SimpleRateEntity> rates) {
        ArrayList<String> names = new ArrayList<>();
        for (SimpleRateEntity rate: rates){
            if (rate!= null && rate.getName() != null){
                names.add(rate.getName());
            }
        }
        return names;
    }

    public static Date getDateFromServerFormat(String dateStr){
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault());
        Date date;
        try {
            date = serverDateFormat.parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
    }

    public static String getServerFormatFromDate(Date date){
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault());
        return serverDateFormat.format(date);
    }

    public static String getUserFormatFromDate(Date date){
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(USER_DATE_FORMAT, Locale.getDefault());
        return serverDateFormat.format(date);
    }

    public static String getUserFullFormatFromDate(Date date){
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(USER_FULL_DATE_FORMAT, Locale.getDefault());
        return serverDateFormat.format(date);
    }

    public static SimpleRateEntity findSimpleRateEntity(String name, ArrayList<SimpleRateEntity> list) {
        for(SimpleRateEntity rateEntity : list) {
            if(rateEntity.getName().equals(name)) {
                return rateEntity;
            }
        }
        return null;
    }
}
