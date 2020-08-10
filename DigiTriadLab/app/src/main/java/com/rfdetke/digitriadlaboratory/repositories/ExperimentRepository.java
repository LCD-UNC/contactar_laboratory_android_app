package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao.ExperimentDone;
import com.rfdetke.digitriadlaboratory.database.daos.TagDao;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.ExperimentTag;
import com.rfdetke.digitriadlaboratory.database.entities.Tag;

import java.util.List;

public class ExperimentRepository {
    private ExperimentDao experimentDao;
    private LiveData<List<ExperimentDone>> allExperimentDone;
    private TagDao tagDao;

    public ExperimentRepository(AppDatabase database) {
        experimentDao = database.getExperimentDao();
        tagDao = database.getTagDao();
        allExperimentDone = experimentDao.getLiveDataExperimentDone();
    }

    public LiveData<List<ExperimentDone>> getAllExperimentDone() {
        return allExperimentDone;
    }

    public long insert(Experiment experiment) {
        return experimentDao.insert(experiment);
    }

    public void delete(Experiment experiment) {
        experimentDao.delete(experiment);
    }

    public Experiment getLast() {
        return experimentDao.getLastExperiment();
    }

    public Experiment getById(long id) { return experimentDao.getExperimentById(id); }

    public List<String> getTagList() {
        return tagDao.getTagList();
    }

    public long insertTag(String tagValue) {
        return tagDao.insert(new Tag(tagValue));
    }

    public void relateToTag(long experimentId, long tagId) {
        tagDao.insertRelation(new ExperimentTag(experimentId, tagId));
    }

    public long getTagId(String tagValue) {
        return tagDao.getIdByTag(tagValue);
    }

    public List<String> getTagList(long experimentId) {
        return tagDao.getTagList(experimentId);
    }
}
