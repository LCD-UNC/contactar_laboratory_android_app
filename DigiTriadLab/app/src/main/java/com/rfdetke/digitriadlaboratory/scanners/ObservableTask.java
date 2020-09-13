package com.rfdetke.digitriadlaboratory.scanners;

public interface ObservableTask {
    public void addObserver(TaskObserver taskObserver);
    public void removeObserver(TaskObserver taskObserver);
    public void setDone();
}
