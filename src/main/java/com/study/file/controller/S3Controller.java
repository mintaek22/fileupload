package com.study.file.controller;

import com.study.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/file")
    public void uploadFile(@RequestParam("files") List<MultipartFile> multipartFiles)
            throws IOException {
        s3Service.saveUploadFiles(multipartFiles);
    }

    @GetMapping("/file")
    public String download(@RequestParam String fileName) {
        return s3Service.getObject(fileName);
    }
}
