package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@Data
@Document(collection = "file-data")
public class FileRecords {
    @Id
    private String id;
    @Indexed
    private String fileId;
    private LocalDate date;
    private String client;
    private String module;
    private String reference;
    private int noOfHours;
    private String email;
    private String taskDescription;
    private String taskType;

}
