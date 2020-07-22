package com.rfdetke.digitriadlaboratory;

import java.util.ArrayList;
import java.util.List;

public interface DataBucket {
    List<Object> records = new ArrayList<>();

    void cacheDataToList();
    List<Object> getRecordsList();
}
