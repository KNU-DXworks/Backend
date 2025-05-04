package project.DxWorks.domain.profile.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // profile id

    // TODO : User 와 one to one mapping


    @Column(length = 1000)
    private String introduce; //자기소개

    private String bodyTypeBefore; //전 그룹
    private String bodyTypeAfter; // 후 그룹

    private Double goalWeight; // 목표 체중 (회원가입시 작성)
    private Double goalMuscle; // 목표 골격근량
    private Double goalFat; // 목표 체지방

    // TODO : 지갑 주소를 저장? 잘 몰라서 보류
    // private String walletAddress;

    // TODO : 인바디 수치 (inbody domain 에서 받아와야함)
//    private LocalDateTime lastUpdated; //마지막 인바디 분석 일시
//
//    private LocalDateTime created; // 생성 시간
//    private LocalDateTime updated; // 수정 시간
//
//    @PrePersist
//    public void prePersist() {
//        this.created = this.updated = LocalDateTime.now(); //처음 인바디 측정 시 초기화.
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updated = LocalDateTime.now(); // update 됐을 때 시간 수정
//    }


}
