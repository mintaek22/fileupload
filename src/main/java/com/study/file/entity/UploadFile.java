package com.study.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class UploadFile {

    @Id
    private String Id;

    private String fileName;

    private String storeFileUrl;

}
