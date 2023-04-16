package com.example.demo.services;

import com.example.demo.model.FileData;
import com.example.demo.model.FileRecords;
import com.example.demo.parser.FileParser;
import com.example.demo.parser.ParserContext;
import com.example.demo.parser.ParserFactory;
import com.example.demo.parser.excel.ExcelParserContext;
import com.example.demo.repositories.FileDataRepository;
import com.example.demo.repositories.FileRecordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileRecordsRepository fileRecordsRepository;

    public void uploadFile(MultipartFile file){
        String fileId = "";
        Optional<FileData> optionalFileData = fileDataRepository.findFileDataByFileName(file.getOriginalFilename());
        if(optionalFileData.isEmpty()){
            FileData data = FileData.builder()
                    .id(UUID.randomUUID().toString())
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .build();
            FileData savedData = fileDataRepository.save(data);
            fileId = savedData.getId();
        } else {
            fileId = optionalFileData.get().getId();
        }


        ParserContext context = ExcelParserContext
                .builder()
                .fileId(fileId)
                .build();

        FileParser parser = ParserFactory.getParser(context);
        try {
            List<FileRecords> fileRecords = parser.parseEntries(file.getInputStream());
            fileRecordsRepository.saveAll(fileRecords);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
