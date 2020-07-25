package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.SourceType;

@Dao
public interface SourceTypeDao {

    @Query("SELECT * FROM source_type WHERE type==(:type)")
    SourceType getSourceTypeByType(String type);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(SourceType sourceType);

    @Delete
    void delete(SourceType sourceType);
}
