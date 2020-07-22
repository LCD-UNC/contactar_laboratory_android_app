package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.SourceType;

@Dao
public interface SourceTypeDao {

    @Query("SELECT * FROM source_type WHERE type==(:type)")
    SourceType getSourceTypeByType(String type);

    @Insert
    long insert(SourceType sourceType);

    @Delete
    void delete(SourceType sourceType);
}
