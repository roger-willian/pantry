package org.pantry.shopping.databases;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.pantry.shopping.entities.ListItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CSVGateway<T> {
    protected final List<T> items = new ArrayList<>();
    private final String fileName;
    private final CSVFormat format;

    public CSVGateway(String fileName) {
        this.fileName = fileName;
        CSVFormat.Builder formatBuilder = CSVFormat.Builder.create(CSVFormat.EXCEL);
        formatBuilder.setDelimiter(";");
        format = formatBuilder.build();
    }

    private File getFile() throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    protected void loadItems() {
        try (Reader in = new FileReader(getFile())) {
            Iterable<CSVRecord> records = format.parse(in);
            items.clear();
            for (CSVRecord record : records) {
                items.add(loadItem(record));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract T loadItem(CSVRecord record);

    protected void storeItems() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getFile()), format)) {
            for (T item : items) {
                printer.printRecord(storeItem(item));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract Iterable<Object> storeItem(T item);
}
