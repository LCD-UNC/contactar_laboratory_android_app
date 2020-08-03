package com.rfdetke.digitriadlaboratory.contacthandlers;

public interface ObservableScanner {
    public void addObserver(ScanObserver scanObserver);
    public void removeObserver(ScanObserver scanObserver);
    public void setDone();
}
