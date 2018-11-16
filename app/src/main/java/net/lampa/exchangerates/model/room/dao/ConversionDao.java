package net.lampa.exchangerates.model.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.lampa.exchangerates.model.entities.room.ConversionEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ConversionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConversion(ConversionEntity conversion);

    @Delete
    void deleteConversion(ConversionEntity conversion);

    @Query("DELETE FROM conversion")
    void clearConversionHistory();

    @Query("SELECT * FROM conversion")
    Single<List<ConversionEntity>> getAllConversions();
}
