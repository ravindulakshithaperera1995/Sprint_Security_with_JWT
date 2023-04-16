package com.example.demo.parser.sample;

import com.example.demo.model.FileRecords;
import com.example.demo.parser.FileParser;
import com.example.demo.parser.Record;
import com.example.demo.parser.excel.ExcelParserContext;
import com.example.demo.parser.excel.ExcelSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SampleParser implements FileParser, RecordProcessor<Record, FileRecords> {
   String[] headers = {"Timestamp", "Date", "Client", "Module", "Reference ID ", "Effort Spent",
           "Email Address", "Task Description", "Task Type" };
    private final ExcelParserContext context;

    public SampleParser(ExcelParserContext context) {
        this.context = context;
    }

    @Override
    public List<FileRecords> parseEntries(InputStream inputStream) {
        try {
            log.debug("Started reading rows");
            Iterable<Row> rows = ExcelSupport.getRows(inputStream, context);
            if(rows != null){
                Row header = ExcelSupport.getHeaderRow(rows);
                ExcelSupport.checkHeader(header, headers, context);
                return ExcelSupport.parseContent(context, rows, 0, this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Collections.emptyList();
    }

    @Override
    public List<FileRecords> processRecord(Record record) {
        log.debug("Started generating records");
        List<FileRecords> records = new ArrayList<>();
        FileRecords fileRecord = convertRecord(record);
        records.add(fileRecord);

        log.debug("Finishing generating records");
        return records;
    }

    private FileRecords convertRecord(Record record){
        return FileRecords.builder()
                .client(record.getString("Client"))
                .fileId(context.getFileId())
                .module(record.getString("Module"))
                .date(record.getDate("Date", false))
                .reference(record.getString("Reference ID"))
                .noOfHours(record.getInt("Effort Spent", false).intValue())
                .email(record.getString("Email Address"))
                .taskDescription(record.getString("Task Description"))
                .taskType(record.getString("Task Type"))
                .build();
    }
}
