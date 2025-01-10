package com.example.demo.Common;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy.MM.dd HH시 mm분", timezone = "Asia/Seoul")
    private Timestamp createdDate;

    @LastModifiedDate @JsonFormat(pattern = "yyyy.MM.dd HH시 mm분", timezone = "Asia/Seoul")
    private Timestamp modifiedDate;

    @JsonFormat(pattern = "yyyy.MM.dd HH시 mm분", timezone = "Asia/Seoul")
    private Timestamp deletedDate;
}
