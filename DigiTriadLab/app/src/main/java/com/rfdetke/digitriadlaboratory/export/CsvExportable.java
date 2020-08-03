package com.rfdetke.digitriadlaboratory.export;

public interface CsvExportable {
    public String csvHeader();
    public String toCsv();
}
