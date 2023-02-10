package com.study.file.repository;

import com.study.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UploadFileRepository extends JpaRepository<UploadFile,String> {
    UploadFile findByFileName(@Param("fileName") String fileName);
}
