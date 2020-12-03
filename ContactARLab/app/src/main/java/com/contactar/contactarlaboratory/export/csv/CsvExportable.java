package com.contactar.contactarlaboratory.export.csv;

public interface CsvExportable {
    public String csvHeader();
    public String toCsv();
}
