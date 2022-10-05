package com.example.eduwithbe.user.domain;

import com.example.eduwithbe.Study.Domain.StudyApplyEntity;
import com.example.eduwithbe.Study.Domain.StudyRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringEntity;
import com.example.eduwithbe.mentoring.domain.MentoringRecruitmentEntity;
import com.example.eduwithbe.mentoring.domain.MentoringScrapEntity;
import com.example.eduwithbe.notice.domain.NoticeEntity;
import com.example.eduwithbe.user.dto.UserSaveDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "email", unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private char gender;

    @Column(name = "address")
    private String address;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int stamp;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int point;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int day;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<NoticeEntity> noticeEntities = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private final List<MentoringEntity> mentoringEntities = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private final List<MentoringEntity> mentoringEntities2 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<UserAttendanceEntity> userAttendanceEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public final List<MentoringRecruitmentEntity> mentoringRecruitments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MentoringScrapEntity> mentoringScrap = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<StudyRecruitmentEntity> studyRecruitments = new ArrayList<>();


    // 모집글 작성
    public void addStudyRecruitment(StudyRecruitmentEntity study) {
        this.studyRecruitments.add(study);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<StudyApplyEntity> studyApplies = new ArrayList<>();

//    public void newMentoringRecruitment(MentoringRecruitmentEntity m){
//        this.mentoringRecruitments.
//    }

    public static UserEntity saveUser(UserSaveDTO userSaveDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(userSaveDTO.getEmail());
        userEntity.setName(userSaveDTO.getName());
        userEntity.setPwd(userSaveDTO.getPwd());
        userEntity.setAge(userSaveDTO.getAge());
        userEntity.setGender(userSaveDTO.getGender());
        userEntity.setAddress(userSaveDTO.getAddress());
        userEntity.setRoles(Collections.singletonList("ROLE_USER"));

        return userEntity;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return pwd;
    }


    @Override
    public String getUsername() {
        return email;
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
