package com.easynetworks.lotteFactoring.Domain;

import com.easynetworks.lotteFactoring.Common.BaseTimeEntity;
import com.easynetworks.lotteFactoring.Common.ExcelColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ExcelColumn
    private String ip;

    @ExcelColumn
    private String locale;

    @ExcelColumn
    private String device;

    private String macAddress;

    @ExcelColumn
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType action;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @Builder
    public UserHistory(String ip, String locale, String device, String macAddress, ActivityType action, User user) {
        this.ip = ip;
        this.locale = locale;
        this.device = device;
        this.macAddress = macAddress;
        this.action = action;
        this.user = user;
    }
}
