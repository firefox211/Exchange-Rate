package net.lampa.exchangerates.model.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.lampa.exchangerates.model.entities.room.ConversionEntity;
import net.lampa.exchangerates.model.room.dao.ConversionDao;



@Database(entities = {ConversionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ConversionDao conversionDao();

}
