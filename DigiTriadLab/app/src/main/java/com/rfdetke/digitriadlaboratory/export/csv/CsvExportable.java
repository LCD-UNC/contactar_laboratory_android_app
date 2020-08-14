package com.rfdetke.digitriadlaboratory.export.csv;

public interface CsvExportable {
    public String csvHeader();
    public String toCsv();
}
