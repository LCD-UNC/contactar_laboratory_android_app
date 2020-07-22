package com.rfdetke.digitriadlaboratory;

import android.content.BroadcastReceiver;

import java.util.List;

public abstract class BroadcastReceiverDataBucket extends BroadcastReceiver implements DataBucket {
    @Override
    public List<Object> getRecordsList() {
        return records;
    }
}
