package com.study.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.study.file.entity.UploadFile;
import com.study.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final UploadFileRepository uploadFileRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void saveUploadFiles(List<MultipartFile> multipartFile){
        multipartFile.forEach(this::saveUploadFile);
    }

    public void saveUploadFile(MultipartFile multipartFile){
        //파일 타입과 사이즈 저장
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        //파일 이름
        String originalFilename = multipartFile.getOriginalFilename();
        //파일 이름이 비어있으면 (assert 오류 반환)
        assert originalFilename != null;

        //확장자
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        //파일이름이 겹치지 않게
        String key = String.valueOf(UUID.randomUUID());
        String s3name = originalFilename+"_"+key;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, s3name, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            //파일을 제대로 받아오지 못했을때
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3.getUrl(bucket,s3name).toString().replaceAll("\\+", "+");
        UploadFile uploadFile = new UploadFile(key,originalFilename,storeFileUrl);
        uploadFileRepository.save(uploadFile);
    }

    public String getObject(String fileName){
        return uploadFileRepository.findByFileName(fileName).getStoreFileUrl();
    }

}