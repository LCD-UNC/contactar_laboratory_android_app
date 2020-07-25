package com.rfdetke.digitriadlaboratory.scanners;

public interface ObservableScanner {
    public void addObserver(ScanObserver scanObserver);
    public void removeObserver(ScanObserver scanObserver);
    public void setDoneScanning();
}
