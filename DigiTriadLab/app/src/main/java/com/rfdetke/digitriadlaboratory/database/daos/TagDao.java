package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.ExperimentTag;
import com.rfdetke.digitriadlaboratory.database.entities.Tag;

import java.util.List;

@Dao
public interface TagDao {

    @Query("SELECT id FROM tag WHERE tag==(:tag)")
    long getIdByTag(String tag);

    @Query("SELECT tag FROM tag")
    List<String> getTagList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Tag tag);

    @Insert
    void insertRelation(ExperimentTag experimentTag);

    @Query("SELECT t.tag FROM experiment_tag AS et " +
            "JOIN tag AS t ON et.tag_id=t.id " +
            "WHERE et.experiment_id=(:experimentId)" )
    List<String> getTagList(long experimentId);
}
