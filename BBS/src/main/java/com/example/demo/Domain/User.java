package com.easynetworks.lotteFactoring.Domain;


import com.easynetworks.lotteFactoring.Common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message="아이디는 영어소문자로만 필수 입력값입니다.")
    @Pattern(regexp="[a-z]+", message = "아이디는 영어 소문자로만 입력해주세요.")
    @Column(nullable = false,length = 20, name="username")
    private String username;

    @NotBlank(message="이름은 필수 입력값입니다.")
    @Column(length = 25)
    private String name;

    @JsonIgnore
//    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}", message = "비밀번호는 영어와 숫자로 포함해서 8~15자리 이내로 입력해주세요.")
//    @NotBlank(message="비밀번호는 필수값입니다.")
    @Column(nullable = false)
    private String password;


    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Column(nullable = false)
    private String email;

    @Column(length = 20)
    private String accessIp;

    @Column(name = "access_date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime accessDate;

    private int failCount;
    private boolean isEnabled = true;

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Enumerated(EnumType.STRING)
    @NotNull(message="사용자 권한은 필수값입니다.")
    private UserRole userRole = UserRole.USER;

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp;
    }

    public void setAccessDate(LocalDateTime accessDate) {
        this.accessDate = accessDate;
    }

    @Builder
    public User(String name, String username, String password, String email, UserRole role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
